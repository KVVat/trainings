package com.example.popularmovies.constants;

import com.example.popularmovies.R;

public enum MovieSortMode {
    SORT_MODE_POPULAR(0,"popular", R.string.most_popular,R.id.action_sort_popularity),
    SORT_MODE_TOPRATED(1,"top_rated",R.string.top_rated,R.id.action_sort_toprated),
    SORT_MODE_FAVORITE(2,"favorite",R.string.favorites,R.id.action_items_favorite);

    private int id;

    private String key;

    private int titleRes;

    private int menuRes;

    MovieSortMode(int id,String key,int titleRes,int menuRes){
        this.id = id;
        this.key = key;
        this.titleRes=titleRes;
        this.menuRes=menuRes;
    }

    public String getKey() {
        return this.key;
    }
    public int getId() {
        return this.id;
    }
    public int getTitleRes(){
        return this.titleRes;
    }
    public int getMenuRes(){
        return this.menuRes;
    }
    public static MovieSortMode valueOf(int id) {
        MovieSortMode[] array = values();
        for(MovieSortMode num : array) {
            if (id == num.getId()){
                return num;
            }
        }
        return null;
    }
}
