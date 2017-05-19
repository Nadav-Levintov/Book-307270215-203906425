package db_utils;



import com.google.inject.Inject;
import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;

import java.util.*;


public class DataBaseImpl implements DataBase {


    private final Integer num_of_columns;
    private final Integer num_of_keys;
    private final List<String> names_of_columns;
    private final LineStorageFactory lineStorageFactory;


    public DataBaseImpl(Integer num_of_keys, List<String> names_of_columns, LineStorageFactory lineStorageFactory) {
        this.num_of_keys=num_of_keys;
        this.names_of_columns = names_of_columns;
        this.num_of_columns=names_of_columns.size();
        this.lineStorageFactory = lineStorageFactory;
    }

    public void build_db(String csv_data){
        List<Map<String,String>> DB= new ArrayList<>();
        for (int i=0;i<num_of_keys;i++)
        {
            DB.add(i,new TreeMap<String,String>());
        }

        String[] lines = csv_data.split("\n");
        for(String line : lines){
            String[] curr_line = line.split(",");
            //TODO: make generic with permutations
            String key_1=curr_line[0]+","+curr_line[1];
            String key_2=curr_line[1]+","+curr_line[0];
            String value = new String();
            for(int i=num_of_keys;i<num_of_columns-1;i++)
            {
                value+=curr_line[i]+",";
            }
            value+=curr_line[num_of_columns-1];

            DB.get(0).put(key_1,value);
            DB.get(1).put(key_2,value);

        }

        for(int i=0; i<num_of_keys;i++)
        {
            LineStorage lineStorage = lineStorageFactory.open(names_of_columns.get(i));
            Map<String,String> curr_table = DB.get(i);
            for(Map.Entry<String,String> entry : curr_table.entrySet())
            {
                String output = entry.getKey()+","+entry.getValue();
                lineStorage.appendLine(output);
            }
        }

    }


    public Optional<String> get_val_from_column_by_name(List<String> keys, String column) {
        LineStorage lineStorage = lineStorageFactory.open(names_of_columns.get(0));
        Integer low=0,high;
        String curr_line;
        try {
            high = lineStorage.numberOfLines()-1;
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        String key=new String();
        for (String str: keys
             ) {
            key+=str+",";
        }

        while (low <= high) {

            Integer mid = low + (high - low) / 2;
            try {
                curr_line = lineStorage.read(mid);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }

            String[] values = curr_line.split(",");
            //TODO: do dynamically
            String curr_key=values[0]+","+values[1]+",";
            Integer compare=key.compareTo(curr_key);
            if      (compare < 0) high = mid - 1;
            else if (compare > 0) low = mid + 1;
            else return Optional.of(values[names_of_columns.indexOf(column)]);
        }
        return Optional.empty();
    }

    public List<String> get_lines_for_key(String key,Integer key_id) {
        List<String> results = new ArrayList<>();

        if(!names_of_columns.contains(key))
        {
            return results;
        }

        LineStorage lineStorage = lineStorageFactory.open(names_of_columns.get(key_id));
        Integer low=0,high;
        String curr_line;
        try {
            high = lineStorage.numberOfLines()-1;
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }


        Integer index=0,compare=0;
        String[] values;

        while (low <= high) {

            Integer mid = low + (high - low) / 2;
            try {
                curr_line = lineStorage.read(mid);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }

            values = curr_line.split(",");
            compare=key.compareTo(values[0]);
            if      (compare < 0) high = mid - 1;
            else if (compare > 0) low = mid + 1;
            else index=mid;
        }

        if(low > high) {
            return results;
        }

        do {
            try {
                curr_line = lineStorage.read(index);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            values = curr_line.split(",");
            compare = key.compareTo(values[0]);
            index--;
        }while(compare == 0 && index >= 0);

        index++;
        try {
            do {
                try {
                    curr_line = lineStorage.read(index);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                values = curr_line.split(",");
                compare = key.compareTo(values[0]);
                index++;
                if (compare == 0) {
                    String output = curr_line.substring(curr_line.indexOf(',') + 1);
                    results.add(output);
                }
            }
            while(compare == 0 && index < lineStorage.numberOfLines());
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        return results;
    }

    public Optional<String> get_val_from_column_by_column_number(List<String> keys, Integer column) {
        if (column< 0  || column > num_of_columns)
        {
            return Optional.empty();
        }
        return get_val_from_column_by_name(keys,names_of_columns.get(column));
    }

    public Integer getNum_of_columns() {
        return num_of_columns;
    }

    public Optional<String> get_line_by_num_and_key(Integer num, String key) throws IllegalArgumentException{
        if(!names_of_columns.contains(key))
        {
            return Optional.empty();
        }

        LineStorage lineStorage = lineStorageFactory.open(key);

        try {
            if(num>lineStorage.numberOfLines() || num<0)
            {
                throw new IllegalArgumentException(Integer.toString(num));
            }
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        try {
            return Optional.of(lineStorage.read(num));
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
    }
    public List<String> getNames_of_columns() {
        return names_of_columns;
    }

    public Integer getNum_of_keys() {
        return num_of_keys;
    }

    public OptionalInt get_num_of_column(String col_name)
    {
        if(!names_of_columns.contains(col_name))
        {
            return OptionalInt.empty();
        }

        Integer index = names_of_columns.indexOf(col_name);
        if(index<0)
        {
            return OptionalInt.empty();
        }
        else
        {
            return OptionalInt.of(index);
        }
    }
}
