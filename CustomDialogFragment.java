private final int REQ_CODE = 100;
// Create an anonymous implementation of OnClickListener
private View.OnClickListener voiceListener = new View.OnClickListener() {
    public void onClick(View v) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
        try {
            startActivityForResult(intent, REQ_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(context, "Sorry your device not supported", Toast.LENGTH_SHORT).show();
        }
    }
};

@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            if (resultCode == Activity.RESULT_OK && null != data) {
                ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                extraction(result.get(0).toString());
            } else {
//                extraction("25mcg Fentanyl  IV  bolus  periodically throughout the night.");
                extraction("The cold makes my whole body weak.");
            }
        }
    }

    private void extraction(String text){
        Log.v(TAG, "extraction " + text);
        doPost.extraction(text,new CustomCallback(){
            @Override
            public void onSuccess(Boolean isTrue, JsonArray jsonArray) {
                Log.v(TAG, "extraction " + jsonArray);
                ListView listView = (ListView) getView().findViewById(R.id.list_custom);
                int entitySize = 8;
                int listCount = 0;

                for(int i = 0; i < jsonArray.size(); i++){

                    JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                    Log.v(TAG, "jsonObject " + jsonObject);
//                    listCount = entitySize * i;
                    Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
                    for(Map.Entry<String,JsonElement> entry : entrySet){
                        for(int j = 1; j < entitySize+1; j++){

//                            LinearLayout linearLayout = (LinearLayout)  listView.getChildAt(j+listCount);
                            LinearLayout linearLayout = (LinearLayout)  listView.getChildAt(j);
                            LinearLayout item = (LinearLayout)  linearLayout.getChildAt(0);

                            TextView textView = (TextView) item.getChildAt(0);
                            EditText editText = (EditText) item.getChildAt(1);

//                                Log.v(TAG, "entry.getKey() " + entry.getKey());
//                                Log.v(TAG, "textView " + textView.getText());
//                                Log.v(TAG, "value " + jsonObject.get(entry.getKey()).getAsString());


                            if(textView.getText().toString().equals(entry.getKey())){
                                if(editText!=null){
                                    editText.setText(jsonObject.get(entry.getKey()).getAsString());
                                }
                        }
                    }
                 }
                }

                customAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Boolean isFalse) {

            }

        });
    }