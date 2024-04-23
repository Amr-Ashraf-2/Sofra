package com.ashraf.amr.apps.sofra.data.local.Room;

import android.content.Context;

public class CarFoodMethod {

    private static RoomManger roomManger;
    public static RoomDao roomDao;

    public static void getRoomManager(Context context) {

        roomManger = RoomManger.getInstance(context);
        roomDao = roomManger.roomDao();

    }

//    public static void insertItemToCar(ItemFoodData foodItem, Context context) {
//        getRoomManager(context);
//        new insertItemToCarAsyncTask(roomDao).execute(foodItem);
//
//    }
//
//    public static void updateItemToCar(ProductData foodItem, Context context) {
//        getRoomManager(context);
//        new updateItemToCarAsyncTask(roomDao).execute(foodItem);
//    }
//
//    public static void deleteItemToCar(ItemFoodData foodItem, Context context) {
//        getRoomManager(context);
//        new deleteItemToCarAsyncTask(roomDao).execute(foodItem);
//    }
//
//    public static void deleteAllItemToCar(Context context) {
//        getRoomManager(context);
//        new deleteAllItemAsyncTask(roomDao).execute();
//    }
//
//
//    //AsyncTask
//    private static class insertItemToCarAsyncTask extends AsyncTask<ItemFoodData, Void, Void> {
//
//        private  RoomDao roomDao;
//
//        public insertItemToCarAsyncTask(RoomDao roomDao) {
//            this.roomDao = roomDao;
//        }
//
//        @Override
//        protected Void doInBackground(ItemFoodData... foodMenuData) {
//            roomDao.insertItemToCar(foodMenuData[0]);
//            return null;
//        }
//    }
//
//    private static class deleteItemToCarAsyncTask extends AsyncTask<ItemFoodData, Void, Void> {
//        private  RoomDao roomDao;
//
//        public deleteItemToCarAsyncTask(RoomDao roomDao) {
//            this.roomDao = roomDao;
//        }
//        @Override
//        protected Void doInBackground(ItemFoodData... foodMenuData) {
//            roomDao.deleteItemToCar(foodMenuData[0]);
//            return null;
//        }
//    }
//
//    private static class updateItemToCarAsyncTask extends AsyncTask<ItemFoodData, Void, Void> {
//        private  RoomDao roomDao;
//
//        public updateItemToCarAsyncTask(RoomDao roomDao) {
//            this.roomDao = roomDao;
//        }
//        @Override
//        protected Void doInBackground(ItemFoodData... foodMenuData) {
//            roomDao.updateItemToCar(foodMenuData[0]);
//            return null;
//        }
//    }
//
//    private static class deleteAllItemAsyncTask extends AsyncTask<Void, Void, Void> {
//        private RoomDao roomDao;
//
//        public deleteAllItemAsyncTask(RoomDao roomDao) {
//            this.roomDao = roomDao;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            roomDao.deleteAllItemToCar();
//            return null;
//        }
//    }

//    public static List<FoodMenuData> getAllItem(Context context) {
//        getRoomManager(context);
//new deleteItemToCarAsyncTask(roomDao).execute(foodItem);
//    }
//
//    private static class getAllItemAsyncTask extends AsyncTask<Void, Void, Void> {
//        private  RoomDao roomDao;
//
//        public getAllItemAsyncTask(RoomDao roomDao) {
//            this.roomDao = roomDao;
//        }
//        @Override
//        protected Void doInBackground(Void... voids) {
//
//            return null;
//        }
//    }

}
