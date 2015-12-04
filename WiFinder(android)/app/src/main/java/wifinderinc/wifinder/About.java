package wifinderinc.wifinder;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Cole Baughn on 12/3/2015.
 */
public class About extends AppCompatActivity {
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        textView = (TextView)findViewById(R.id.txtBody);
        textView.setText("Contributors:\n     Andrew Sytsma\n     Rayten\n     Cole Baughn\n     Michael Young\n     Zeyu Pan\n     Vishal Gill\n\n\nContact e-mail: MonsterChat307@gmail.com");
    }
}
