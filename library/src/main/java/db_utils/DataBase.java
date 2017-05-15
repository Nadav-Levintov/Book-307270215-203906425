package db_utils;



import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;



public class DataBase {


    protected Integer num_of_columns;

    public List<String> getNames_of_columns() {
        return names_of_columns;
    }

    protected List<String> names_of_columns;
    protected Map<String,String> DB = new TreeMap();
    protected StorageInterface storage_implament;
    public DataBase(List<String> names_of_columns){
        this.names_of_columns = names_of_columns;
        this.num_of_columns=names_of_columns.size();
        this.storage_implament = new DefaultStorageImplament();
    }

    public DataBase(List<String> names_of_columns,StorageInterface storage_implemant) {

        this.names_of_columns = names_of_columns;
        this.num_of_columns=names_of_columns.size();
        this.storage_implament = storage_implemant;
    }

    public void build_db(String csv_data){

        String[] lines = csv_data.split("\n");
        for(String line : lines){
            String key = line.substring(0,line.indexOf(','));
            String value = line.substring(line.indexOf(',')+1);
            DB.put(key,value);
        }

        for(Map.Entry<String,String> entry : DB.entrySet())
        {
            String output = entry.getKey()+ ',' + entry.getValue();
            storage_implament.appendLine(output);
        }
    }

    public Integer size() throws InterruptedException {
        return storage_implament.numberOfLines();
    }

    public Optional<String> get_val_from_column_by_name(String key, String column) throws InterruptedException {
        Integer low=0,high;
        String curr_line;
        high = storage_implament.numberOfLines()-1;

        while (low <= high) {

            Integer mid = low + (high - low) / 2;
            curr_line = storage_implament.read(mid);

            String[] values = curr_line.split(",");
            Integer compare=key.compareTo(values[0]);
            if      (compare < 0) high = mid - 1;
            else if (compare > 0) low = mid + 1;
            else return Optional.of(values[names_of_columns.indexOf(column)]);
        }
        return Optional.empty();
    }

    public Optional<String> get_val_from_column_by_colum_number(String key, Integer column) throws InterruptedException {
        Integer low=0,high;
        String curr_line;
        high = storage_implament.numberOfLines()-1;

        while (low <= high) {

            Integer mid = low + (high - low) / 2;
            curr_line = storage_implament.read(mid);

            String[] values = curr_line.split(",");
            Integer compare=key.compareTo(values[0]);
            if      (compare < 0) high = mid - 1;
            else if (compare > 0) low = mid + 1;
            else return Optional.of(values[column]);
        }
        return Optional.empty();
    }

    public Integer getNum_of_columns() {
        return num_of_columns;
    }

    public Optional<String> get_line_by_num(Integer num) throws InterruptedException, IllegalArgumentException{
        if(num>=this.size() || num<0)
        {
            throw new IllegalArgumentException(Integer.toString(num));
        }
        return Optional.of(storage_implament.read(num));
    }
    /*
    public DataBase(Integer num_of_columns,List<?> names_of_columns) {

        this.num_of_columns = num_of_columns;
        this.names_of_columns = names_of_columns;
        this.storage_implament = new DefaultStorageImplament();
    }
    public DataBase(Integer num_of_columns,List<?> names_of_columns,StorageInterface storage_implament) {

        this.num_of_columns = num_of_columns;
        this.names_of_columns = names_of_columns;
        this.storage_implament = storage_implament;
    }


    public Integer getNum_of_columns() {
        return num_of_columns;
    }


    public void insert_line(String key,List<String> values)
    {
        if(values.size() != num_of_columns-1)
        {
            throw new IllegalArgumentException();
        }

        DB.put(key,values);
    }

    public void write_to_disk()
    {
        for(Map.Entry<String,List<String>> entry : DB.entrySet())
        {
            String key = entry.getKey();
            List<String> val = entry.getValue();

            String output = key;
            for (String str : val ) {
                output+="," + str;
            }

            storage_implament.appendLine(output);
        }
    }

    public Optional<String> get_val_from_column(String key, String column) throws InterruptedException {
        Integer low=0,high;
        String curr_line;
        high = storage_implament.numberOfLines()-1;

        while (low <= high) {

            Integer mid = low + (high - low) / 2;
            curr_line = storage_implament.read(mid);

            String[] values = curr_line.split(",");
            Integer compare=key.compareTo(values[0]);
            if      (compare < 0) high = mid - 1;
            else if (compare > 0) low = mid + 1;
            else return Optional.of(values[names_of_columns.indexOf(column)]);
        }
        return Optional.empty();
    }

    public Integer size()
    {
        return DB.size();
    }
*/
}
