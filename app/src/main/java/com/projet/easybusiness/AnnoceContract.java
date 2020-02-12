package com.projet.easybusiness;

import android.provider.BaseColumns;

import java.util.Date;

public final class AnnoceContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private AnnoceContract() {}

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "annonce";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_TITRE = "titre";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PRIX = "prix";
        public static final String COLUMN_NAME_PSEUDO = "pseudo";
        public static final String COLUMN_NAME_EMAIL = "emailContact";
        public static final String COLUMN_NAME_TELEPHONE = "telContact";
        public static final String COLUMN_NAME_VILLE = "ville";
        public static final String COLUMN_NAME_CODE_POSTAL = "codePostal";
        public static final String[] COLUMN_NAME_IMAGES = {};
        public static final String COLUMN_NAME_DATE = "date";
    }
}
