package br.com.aj.truco;

import br.com.aj.truco.dao.AppRoomDatabase;
import br.com.aj.truco.util.Constantes;

public class AppConfig {

    public static class AppBuild {
        public static boolean DEBUG = BuildConfig.DEBUG;
//        public static String APPLICATION_ID = BuildConfig.APPLICATION_ID;
//        public static String BUILD_TYPE = BuildConfig.BUILD_TYPE;
        public static String VERSION_NAME = Constantes.VERSAO;
        public static int VERSION_BANCO = AppRoomDatabase.DBVERSION;

    }


}
