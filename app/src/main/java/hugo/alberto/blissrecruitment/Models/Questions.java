package hugo.alberto.blissrecruitment.Models;

import java.util.List;

/**
 * Created by Alberto on 28/05/2016.
 */
public class Questions {
    public String id;
    public String question;
    public String image_url;
    public String thumb_url;
    public String published_at;
    public List<Choices> choices;
}