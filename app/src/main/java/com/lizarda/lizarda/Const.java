package com.lizarda.lizarda;

/**
 * Created by arifinfrds on 11/20/17.
 */

public interface Const {

    String BUTTON_ID_KEY = "BUTTON_ID_KEY";

    String NOT_SET = "not set";

    String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";

    String KEY_KATEGORI = "KEY_KATEGORI";

    interface TAG {
        String URI = "TAG_URI";
        String SPINNER_KATEGORI = "SPINNER_KATEGORI";

        String DOWNLOAD_IMAGE = "DOWNLOAD_IMAGE";
    }

    interface FIREBASE {

        String CHILD_USER = "User";

        String CHILD_PRODUCT = "Product";

        String CHILD_IMAGES = "images";
    }
}
