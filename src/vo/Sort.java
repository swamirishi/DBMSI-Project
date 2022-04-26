package vo;

import com.google.gson.annotations.SerializedName;

public class Sort {
    @SerializedName(Constants.SORT_ORDER)
    private int sortOrder=0;
    @SerializedName(Constants.SORT_NODE_POS)
    private int sortNodePosition;
    @SerializedName(Constants.SORT_NUM_PAGES)
    private int numberOfPages;
    
    public Sort(int sortOrder, int sortNodePosition, int numberOfPages) {
        this.sortOrder = sortOrder;
        this.sortNodePosition = sortNodePosition;
        this.numberOfPages = numberOfPages;
    }
    
    public int getSortOrder() {
        return sortOrder;
    }
    
    public int getSortNodePosition() {
        return sortNodePosition;
    }
    
    public int getNumberOfPages() {
        return numberOfPages;
    }
}
