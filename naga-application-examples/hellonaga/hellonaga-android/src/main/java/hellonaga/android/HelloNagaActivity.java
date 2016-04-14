package hellonaga.android;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import hellonaga.logic.HelloNagaLogic;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.client.android.AndroidPlatform;
import naga.core.util.function.Consumer;

public class HelloNagaActivity extends Activity {

    static {
        AndroidPlatform.register(); // using explicit registration as the ServiceLoader has an issue on Android (META-INF is excluded from the apk)
    }
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Platform.setWebLogger(new Consumer<String>() {
            @Override
            public void accept(String message) {
                HelloNagaActivity.this.displayMessage(message);
            }
        });
        HelloNagaLogic.runClient();
    }

    private void displayMessage(String message) {
        // Displaying the message
        TextView textView = (TextView) findViewById(R.id.helloNagaTextView);
        textView.setText(message);
    }
}
