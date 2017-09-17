package hugo.alberto.blissrecruitment.Interfaces;


import hugo.alberto.blissrecruitment.Models.Health;
import hugo.alberto.blissrecruitment.Models.Post;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Alberto on 29/05/2016.
 */
public interface ShareService {
    public static final String BASE_URL= "https://private-anon-74261c4c7e-blissrecruitmentapi.apiary-mock.com/";
    @FormUrlEncoded
    @POST("share")
    Call<Post> loadDetail(@Field("email") String email,
                          @Field("url") String url);

}
