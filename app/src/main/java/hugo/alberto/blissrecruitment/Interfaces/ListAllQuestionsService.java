package hugo.alberto.blissrecruitment.Interfaces;

import java.util.List;
import hugo.alberto.blissrecruitment.Models.Question;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Alberto on 29/05/2016.
 */
public interface ListAllQuestionsService {
    
    public static final String BASE_URL= "https://private-anon-74261c4c7e-blissrecruitmentapi.apiary-mock.com/";
    @GET("questions?&&")
    Call<List<Question>> getQuestions();

}
