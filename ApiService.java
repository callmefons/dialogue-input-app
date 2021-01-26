@FormUrlEncoded
@POST
Observable<Response<JsonArray>> extraction(@Url String url, @Field("text") String text);
