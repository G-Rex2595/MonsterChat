package wifinderinc.wifinder;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
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
import java.util.ArrayList;

/**
 * Created by Cole Baughn on 12/1/2015.
 */
public class ChatList extends ArrayAdapter<String> {
    private Activity Cont;
    private ArrayList<String> Headers;
    private ArrayList<String> Messages;
    private ArrayList<BitmapDrawable> Images;

    private int TextColor;
    private Typeface FontStyle;

    public ChatList(Activity context,ArrayList<String> head, ArrayList<String> message,  ArrayList<BitmapDrawable> images , int txtColor, Typeface fontStyle){
        super(context, R.layout.chat_list, head);
        Cont = context;
        Headers = head;
        Messages = message;
        Images = images;
        TextColor = txtColor;
        FontStyle = fontStyle;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = Cont.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.chat_list, null, true);

        TextView txtHeader = (TextView) rowView.findViewById(R.id.head);
        TextView txtMessage = (TextView) rowView.findViewById(R.id.msg);

        txtHeader.setTextColor(TextColor);
        txtMessage.setTextColor(TextColor);
        txtHeader.setTypeface(FontStyle);
        txtMessage.setTypeface(FontStyle);

        txtHeader.setText(Headers.get(position));
        txtMessage.setText(Messages.get(position));

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        BitmapDrawable currImg = Images.get(position);

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


