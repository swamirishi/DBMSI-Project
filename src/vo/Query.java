package vo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Query {
    
    @SerializedName(Constants.SORT)
    private Sort sort;
    @SerializedName(Constants.JOIN_1)
    private Join join1;
    @SerializedName(Constants.JOIN_2)
    private Join join2;
    @SerializedName(Constants.FILTERS)
    private List<String> filters;
    
    public Query(Sort sort, Join join1, Join join2) {
        this.sort = sort;
        this.join1 = join1;
        this.join2 = join2;
    }
    
    public Sort getSort() {
        return sort;
    }
    
    public Join getJoin1() {
        return join1;
    }
    
    public Join getJoin2() {
        return join2;
    }
    
    public String getFilter(int pos) {
        return pos<filters.size()?filters.get(pos):"*";
    }
}
