package db_utils;



import com.sun.javaws.exceptions.InvalidArgumentException;
import com.sun.org.apache.xpath.internal.operations.Bool;
import il.ac.technion.cs.sd.book.ext.LineStorage;
import il.ac.technion.cs.sd.book.ext.LineStorageFactory;
import java.lang.RuntimeException;

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

        //String[] lines = csv_data.split("\n");
        List<String> keyList = new ArrayList<>();
        ArrayList<Integer> keyIndexList = new ArrayList<>();
        for(Integer i=0; i<this.num_of_keys; i++)       //create array of index that will make permutations of it
        {
            keyIndexList.add(i);
        }

        keyList.addAll(this.names_of_columns.subList(0,num_of_keys));  //create a list of keys names by order
        Permutations keysPermutation = new Permutations();
        List<List<Integer>> listOfAllPermutations = new ArrayList<>();
        listOfAllPermutations.addAll(keysPermutation.perm(keyIndexList));

        //now listOfAllPermutations has all possible permutations
       for (List<Integer> currentIndexKeyList: listOfAllPermutations)
        {
            String fileName = new String(createFileName(keyList ,currentIndexKeyList));
            write_map_to_new_file(create_file_sorted_by_keys(csv_data, keyList, currentIndexKeyList), fileName);
        }



    }

    private String createFileName(List<String> keyList, List<Integer> premutationIndexList)
    {
        String fileName = new String();
        fileName+=keyList.get(premutationIndexList.get(0));
        for (int index=1; index <keyList.size(); index++)
        {
            fileName+= "_" + keyList.get(premutationIndexList.get(index));
        }
        return fileName;
    }

    //function get list of <all> the keys in the order of sorting and will be saved on disk in that order
    private Map<String,String> create_file_sorted_by_keys(String csv_data, List<String> keys, List<Integer> currentIndexKeyList)
    {
        String[] lines = csv_data.split("\n");
        TreeMap<String,String> map = new TreeMap<>();

        for(String line : lines)
        {
            String[] curr_line = line.split(",");

            //create key for map
            String keysString =new String();
            for (int index=0; index <keys.size(); index++)
            {
                keysString+= curr_line[currentIndexKeyList.get(index)]+",";
            }

            //create value for map
            String value = new String();
            for(int i=num_of_keys;i<num_of_columns-1;i++)
            {
                value+=curr_line[i]+",";
            }
            value+=curr_line[num_of_columns-1];
            map.put(keysString,value);
        }
        return map;
    }

    private void write_map_to_new_file(Map<String,String> map, String fileName)
    {
        LineStorage lineStorage = lineStorageFactory.open(fileName);
        for(Map.Entry<String,String> entry : map.entrySet()) {
            String output = entry.getKey() + entry.getValue();

            lineStorage.appendLine(output);
        }
    }

    public Optional<String> get_val_from_column_by_name(List<String> keys, String column) {
        if(names_of_columns.indexOf(column) <0)
        {
            return Optional.empty();
        }

        String fileName = new String(names_of_columns.get(0));
        for(int i = 1; i< (this.getNum_of_keys()); i++)
        {
            fileName += "_" + names_of_columns.get(i);
        }
        LineStorage lineStorage = lineStorageFactory.open(fileName);

        Integer low=0,high;
        String curr_line;
        try {
            high = lineStorage.numberOfLines()-1;
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        String key=new String();
        for (String str: keys)
        {
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
            String curr_key= new String();
            for(int i = 0; i< this.getNum_of_keys(); i++)
            {
                curr_key += values[i] + ",";
            }


            Integer compare=key.compareTo(curr_key);
            if      (compare < 0) high = mid - 1;
            else if (compare > 0) low = mid + 1;
            else return Optional.of(values[names_of_columns.indexOf(column)]);
        }
        return Optional.empty();
    }

    private Boolean is_all_all_values_in_src_list_are_in_dst_list(List<String> srcList,List<String> dstList )
    {
        for (String src : srcList)
        {
            if(!dstList.contains(src))
                return false;

        }
        return true;
    }
    private Boolean check_if_no_duplicates_in_list(List<String> list)
    {
        List<String> noDuplicates = new ArrayList<>();
        for (String str : list)
        {
            if(noDuplicates.contains(str))
                return false;
            noDuplicates.add(str);
        }
        return true;
    }


    public List<String> get_lines_for_keys(List<String> keysNameList,List<String> keysList)
    {
        ArrayList<String> keysNameforFile = new ArrayList<>(keysNameList);

       if(!is_all_all_values_in_src_list_are_in_dst_list(keysNameList,this.getNames_of_columns().subList(0,this.num_of_keys)))
        {
            throw new IllegalArgumentException();
        }

        if(!check_if_no_duplicates_in_list(keysNameList))
        {
            throw new IllegalArgumentException();
        }

        while(keysNameforFile.size()<(this.num_of_keys-1))//file name is build from all the keys but one
        {
            for(int i=0; i < (this.num_of_keys-1); i++)
            {
                if(!keysNameforFile.contains(this.names_of_columns.get(i)))
                {
                    keysNameforFile.add(names_of_columns.get(i));
                    break;
                }
            }
        }

        String fileName = new String();
        fileName = keysNameforFile.get(0);
        for(int i = 1; i< (keysNameforFile.size()); i++)
        {
            fileName += "_" + keysNameforFile.get(i);
        }

        //here file name is legal

        LineStorage lineStorage = lineStorageFactory.open(fileName);
        List<String> results = new ArrayList<>();

        Integer low=0,high;
        String curr_line;
        Integer index=0,compare=0;

        try {
            high = lineStorage.numberOfLines()-1;
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        String key=new String();
        for (String str: keysList)
        {
            key+=str+",";
        }

        //here have the key to search

        while (low <= high) {
            Integer mid = low + (high - low) / 2;
            try {
                curr_line = lineStorage.read(mid);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }

            String[] values = curr_line.split(",");
            String curr_key= new String();
            for(int i = 0; i< keysList.size(); i++)
            {
                curr_key += values[i] + ",";
            }


            compare=key.compareTo(curr_key);
            if      (compare < 0) high = mid - 1;
            else if (compare > 0) low = mid + 1;
            else index=mid;

        }

        if(low > high) {        //case is not found return empty list
            return results;
        }

        //here find the first line in file with the right key
        do {
            try {
                curr_line = lineStorage.read(index);
            } catch (InterruptedException e) {
                throw new RuntimeException();
            }
            String[] values = curr_line.split(",");
            String curr_key = new String();
            for(int i = 0; i< keysList.size(); i++)
            {
                curr_key += values[i] + ",";
            }
            compare = key.compareTo(curr_key);
            index--;
        }while(compare == 0 && index >= 0);

        index++;

        //here it copys all the rows with the right key from the first
        try {
            do {
                try {
                    curr_line = lineStorage.read(index);
                } catch (InterruptedException e) {
                    throw new RuntimeException();
                }
                String[] values = curr_line.split(",");
                String curr_key = new String();
                for(int i = 0; i< keysList.size(); i++)
                {
                    curr_key += values[i] + ",";
                }
                compare = key.compareTo(curr_key);
                if (compare == 0) {
                    String output = curr_line.substring(key.length() + 1);
                    results.add(output);
                }
                index++;
            }
            while(compare == 0 && index < lineStorage.numberOfLines());
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }

        return results;


    }

    public Optional<String> get_val_from_column_by_column_number(List<String> keys, Integer column) {
        if (column< 0  || column >= num_of_columns)
        {
            return Optional.empty();
        }
        return get_val_from_column_by_name(keys,names_of_columns.get(column));
    }

    public Integer getNum_of_columns() {
        return num_of_columns;
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
        return OptionalInt.of(index);

    }
}
