package com.example.popularmovies.constants;

import com.example.popularmovies.R;

public enum MovieSortMode {
    SORT_MODE_POPULAR(0,"popular", R.string.most_popular),
    SORT_MODE_TOPRATED(1,"top_rated",R.string.top_rated),
    SORT_MODE_FAVORITE(2,"favorite",R.string.favorites);
    private int id;
    private String key;
    private int titleRes;
    MovieSortMode(int id,String key,int titleRes){
        this.id = id;
        this.key = key;
        this.titleRes=titleRes;
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
