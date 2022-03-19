package ForUser;

import Model.Story;
import Model.User;
import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collections;

public class StoryService {

    public static void registerStory(Update update, String orginal, String target, String date, double rateTo, double rateFrom) {
        if (DatabaseStory.story.isEmpty()){
            Story story = new Story();
            story.setUserName(update.getMessage().getFrom().getFirstName());
            story.setDate(date);
            story.setOriginal(orginal);
            story.setTarget(target);
            story.setValuefrom(rateFrom);
            story.setValueto(rateTo*rateFrom);

            DatabaseStory.story.add(story);
            DatabaseStory.map.putIfAbsent(Constant.USER,Collections.singletonList(DatabaseStory.story));
            DatabaseStory.writeJson(Constant.USER);
        }
        else {
            Story story = new Story();
            story.setUserName(update.getMessage().getFrom().getFirstName());
            story.setDate(date);
            story.setOriginal(orginal);
            story.setTarget(target);
            story.setValuefrom(rateFrom);
            story.setValueto(rateTo*rateFrom);

            DatabaseStory.story.add(story);
            DatabaseStory.map.putIfAbsent(Constant.USER,Collections.singletonList(DatabaseStory.story));
           // DatabaseStory.readJson();
            DatabaseStory.writeJson(Constant.USER);
        }


            }

        }

