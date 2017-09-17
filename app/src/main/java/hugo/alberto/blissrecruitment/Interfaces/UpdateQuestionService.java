package hugo.alberto.blissrecruitment.Interfaces;


import hugo.alberto.blissrecruitment.Models.Post;
import hugo.alberto.blissrecruitment.Models.Question;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Alberto on 29/05/2016.
 */
public interface UpdateQuestionService {
    public static final String BASE_URL= "https://private-anon-74261c4c7e-blissrecruitmentapi.apiary-mock.com/";
    @PUT("questions/{id}")
    Call<Question> updateQuestion(@Path("id") int bookId, @Body Question question);

}
