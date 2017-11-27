package com.lizarda.lizarda;

/**
 * Created by arifinfrds on 11/20/17.
 */

public interface Const {

    String KEY_BUTTON_ID = "KEY_BUTTON_ID";

    String NOT_SET = "not set";

    String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";

    String KEY_KATEGORI = "KEY_KATEGORI";

    String KEY_ARRAY_LIST_PRODUCT_ID = "KEY_ARRAY_LIST_PRODUCT_ID";


    interface TAG {
        String URI = "TAG_URI";
        String SPINNER_KATEGORI = "SPINNER_KATEGORI";

        String DOWNLOAD_IMAGE = "DOWNLOAD_IMAGE";

        String TAG_POPULAR = "TAG_POPULAR";

        String TAG_SEARCH = "TAG_SEARCH";
    }

    interface FIREBASE {

        String CHILD_USER = "User";

        String CHILD_PRODUCT = "Product";

        String CHILD_POPULARITY_COUNT = "popularityCount";

        String CHILD_IMAGES = "images";

        String CHILD_COMMENT = "Comment";


        int LIMIT_SUGGEST = 10;

        int LIMIT_NEW_LISTING = 10;

        int PRODUCT_DEFAULT_POPULARITY_COUNT = 0;
    }
}
