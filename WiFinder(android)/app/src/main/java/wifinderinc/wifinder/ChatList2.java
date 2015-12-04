package wifinderinc.wifinder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.security.KeyRep;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by G-Rex on 12/4/2015.
 */
public class ChatList2 extends ArrayAdapter<String>
{
    private Activity Cont;
    private ArrayList<Message> Messages;
    private Boolean TimeStamps;
    private Boolean TimeFormat;
    private SharedPreferences SharedPrefs;

    private int TextColor;
    private Typeface FontStyle;

    public ChatList2(Activity context, ArrayList<Message> messages, ArrayList<String> text, int txtColor, Typeface fontStyle)
    {
        super(context, R.layout.chat_list, text);
        SharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Cont = context;
        Messages = messages;
        TextColor = txtColor;
        FontStyle = fontStyle;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        TimeStamps = SharedPrefs.getBoolean("TimeStampEnabled", false);
        TimeFormat = SharedPrefs.getBoolean("24hrEnabled", false);

        Calendar c = Calendar.getInstance();

        String timeStamp = "";
        if (TimeStamps) {
            SimpleDateFormat formatT = new SimpleDateFormat("hh:mm a");
            if(TimeFormat){
                formatT = new SimpleDateFormat("HH:mm");
            }
            timeStamp = formatT.format(c.getTime());
        }

        LayoutInflater inflater = Cont.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.chat_list, null, true);

        TextView txtHeader = (TextView) rowView.findViewById(R.id.head);
        TextView txtMessage = (TextView) rowView.findViewById(R.id.msg);

        txtHeader.setTextColor(TextColor);
        txtMessage.setTextColor(TextColor);
        txtHeader.setTypeface(FontStyle);
        txtMessage.setTypeface(FontStyle);

        String headerText = String.format("%s:         %s", Messages.get(position).getName(), timeStamp);

        txtHeader.setText(headerText);
        txtMessage.setText(Messages.get(position).getMessage());

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        BitmapDrawable currImg = null;
        if(Messages.get(position) != null)
        {
            currImg = new BitmapDrawable(Cont.getResources(), Messages.get(position).getPicture());
        }

        if(currImg==null){
            RelativeLayout.LayoutParams imgParams = (RelativeLayout.LayoutParams)imageView.getLayoutParams();
            imgParams.height = 0;
            imgParams.topMargin = 0;
            imageView.setLayoutParams(imgParams);

            ViewGroup.LayoutParams msgParams = txtMessage.getLayoutParams();
            txtMessage.setLayoutParams(msgParams);
        }
        else {
            Log.d("Str", currImg.getIntrinsicHeight() + " " + currImg.getIntrinsicWidth());
            imageView.setBackground(currImg);
        }
        return rowView;
    }

    public void setTextColor(int newColor){
        TextColor = newColor;
    }

    public void setFontStyle(Typeface font){
        FontStyle = font;
    }
}
