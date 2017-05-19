package db_utils;



import java.util.*;


public interface DataBase {

    public void build_db(String csv_data);

    public Optional<String> get_val_from_column_by_name(List<String> keys, String column);

    public List<String> get_lines_for_key(String key,Integer key_id);

    public Optional<String> get_val_from_column_by_column_number(List<String> keys, Integer column);

    public Optional<String> get_line_by_num_and_key(Integer num, String key);

    public Integer getNum_of_columns();

    public List<String> getNames_of_columns();

    public Integer getNum_of_keys();

    public OptionalInt get_num_of_column(String col_name);
}
