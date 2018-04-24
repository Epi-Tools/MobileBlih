package blih.epitools.com.mobileblih;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import blih.epitools.com.mobileblih.Activities.AuthActivity;

import android.support.annotation.NonNull;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.core.internal.deps.dagger.internal.Preconditions.checkNotNull;
import static android.support.test.espresso.matcher.RootMatchers.isDialog;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.StringEndsWith.endsWith;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InterfaceBlihTest {
    @Rule
    public ActivityTestRule<AuthActivity> mActivityRule =
            new ActivityTestRule<>(AuthActivity.class);

    /***
     * Connect with epitech account, create a "test" Repo, find the repo with the searchBar, access to acl,
     *  remove acl from ramassage-tek, create acl for "yann.carlen@epitech.eu" and delete repository a the end
     */
    @Test
    public void CompleteScenario() {
        // Enter login/Password to connect
        onView(withId(R.id.email)).perform(typeText(Constant.email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(Constant.pwd), closeSoftKeyboard());
        onView(withId(R.id.valid)).perform(click());

        // If a fab is present we are on MainActivity
        onView(withId(R.id.fab)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        //Create "test" project on MainActivity
        onView(withId(R.id.fab)).perform(click());
        onView(withText("Create Repository")).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withClassName(endsWith("EditText"))).perform(typeText("test"));
        onView(withText("Create Repository")).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // search test and access acl list
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.action_search)).perform(typeSearchViewText("test"));
        onView(withId(R.id.repo_list)).perform(RecyclerViewActions.actionOnItem(
                hasDescendant(withText("test")),
                click()));

        // create acl for yann.carlen@epitech.eu with rw right

        onView(withId(R.id.fab_acl)).perform(click());
        onView(withClassName(endsWith("EditText"))).perform(typeText("yann.carlen@epitech.eu"));
        onView(withId(R.id.spinner_acl_create)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("rw"))).inRoot(isPlatformPopup()).perform(click());
        onView(withText("Add ACL")).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // remove ramassage-tek right

        onView(withId(R.id.acl_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        onView(withId(R.id.spinner_acl_edit)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("None"))).inRoot(isPlatformPopup()).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        // remove repository

        onView(withId(R.id.action_delete)).perform(click());
        onView(withId(android.R.id.button1)).perform(click());
    }

    /***
     *  Connect with epitech account, create a repository named "test" and delete it
     */
    @Test
    public void createRepoFromMainActivity() {
        onView(withId(R.id.email)).perform(typeText(Constant.email), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText(Constant.pwd), closeSoftKeyboard());
        onView(withId(R.id.valid)).perform(click());

        // If a fab is present we are on MainActivity
        onView(withId(R.id.fab)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        //Create "test" project on MainActivity
        onView(withId(R.id.fab)).perform(click());
        onView(withText("Create Repository")).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withClassName(endsWith("EditText"))).perform(typeText("test"));
        onView(withText("Create Repository")).inRoot(isDialog())
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withId(android.R.id.button1)).perform(click());

        // valid
        onView(withId(android.R.id.button1)).perform(click());

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // search test on the list and click on icon
        onView(withId(R.id.action_search)).perform(click());
        onView(withId(R.id.action_search)).perform(typeSearchViewText("test"));

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static ViewAction typeSearchViewText(final String text){
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                //Ensure that only apply if it is a SearchView and if it is visible.
                return allOf(isDisplayed(), isAssignableFrom(SearchView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SearchView) view).setQuery(text,false);
            }
        };
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                View vi = view.findViewById(R.id.delete_repo);
                vi.performClick();
            }
        };
    }
}
