public void extraction(final String text, CustomCallback callback){

    Log.v(TAG, "extraction "+ text);
    if(DoPost.checkNetworkAvailable()) {
        Observable<Response<JsonArray>> repos = HttpManager.getInstance().getService()
                .extraction("", text);

        repos
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Observer<Response<JsonArray>>() {
                    @Override
                    public void onSubscribe(Disposable disposable) {
                    }

                    @Override
                    public void onNext(Response<JsonArray> response) {

                        Log.i(TAG, "onNext");
                        if(response.code() == 200){
                            Log.v(TAG, "extraction"+ response.body());
                            JsonArray jsonArray = response.body();
                            callback.onSuccess(true, jsonArray);

                        }else {
                            Log.v(TAG, "extraction failed"+ response.body());
                            callback.onFailure(false);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.v(TAG, "extraction onError" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onCompleted: sent device ID done");
                        Log.v(TAG, "extraction onCompleted: sent device ID done");
                    }
                });


    }

}