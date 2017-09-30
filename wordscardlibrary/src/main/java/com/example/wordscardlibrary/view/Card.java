package com.example.wordscardlibrary.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wordscardlibrary.R;
import com.example.wordscardlibrary.adapter.WordAdapter;
import com.example.wordscardlibrary.helper.AnimatorHelper;
import com.example.wordscardlibrary.listener.CardListener;
import com.example.wordscardlibrary.model.WordInterface;

import java.util.AbstractList;


public class Card extends CardView implements View.OnTouchListener, CardListener<WordInterface> {

    private Context mContext;

    private final static float RIGHT_DISABLE_COEFFICIENT = 3f / 5;
    private final static float LEFT_DISABLE_COEFFICIENT = 2f / 5;
    private final static float NOT_MOVE_TIME_VALUE = 100f;
    private final static float NOT_MOVE_DISTANCE_VALUE = 1f;

    private float xCurrent;
    private float yCurrent;
    private float xStartCard;
    private float yStartCard;
    private Point sizeWindow;

    private Card cardView;

    private TextView wordTextView;
    private TextView transcriptionTextView;

    private ImageView positiveBtn;
    private ImageView negativeBtn;

    private int positiveBtnResource;
    private int negativeBtnResource;


    private boolean isEnglish = true;
    private boolean DefaultLang = true; //if english is default lang
    private WordInterface current_word;
    private WordAdapter adapter;
    private int backgroundColor;
    private int primaryColor;
    private int back_color;

    public WordAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(WordAdapter adapter) {
        this.adapter = adapter;
        initViews();
        setupAttrs();
        setListeners();
        current_word = (WordInterface) adapter.getRandomItem();
        if (current_word != null) showWordOnCard(current_word);
    }

    private void setListeners() {
        positiveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onPositiveButtonClicked();
            }
        });
        negativeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onNegativeButtonClicked();
            }
        });
    }

    private void setupAttrs() {
        wordTextView.setTextColor(primaryColor);
        cardView.setCardBackgroundColor(backgroundColor);
        if (positiveBtnResource != 0) positiveBtn.setImageResource(positiveBtnResource);
        if (negativeBtnResource != 0) negativeBtn.setImageResource(negativeBtnResource);
    }

    private void initViews() {
        //add 2 textview
        LinearLayout linearLayout_child = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout_child.setOrientation(LinearLayout.VERTICAL);
        linearLayout_child.setLayoutParams(params);
        wordTextView = new TextView(mContext);
        LinearLayout.LayoutParams params_textview = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
        params_textview.weight = 1;
        wordTextView.setLayoutParams(params_textview);
        wordTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 36);
        wordTextView.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        linearLayout_child.addView(wordTextView);

        transcriptionTextView = new TextView(mContext);
        transcriptionTextView.setLayoutParams(params_textview);
        transcriptionTextView.setGravity(Gravity.CENTER | Gravity.TOP);
        linearLayout_child.addView(transcriptionTextView);
        cardView.addView(linearLayout_child);

        //add two buttons
        LinearLayout cardview_parent_layout = (LinearLayout) cardView.getParent();
        LinearLayout buttons_parent_layout = new LinearLayout(mContext);
        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        buttons_parent_layout.setOrientation(LinearLayout.HORIZONTAL);
        buttons_parent_layout.setLayoutParams(params);


        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());


        RelativeLayout layout_btn_left = new RelativeLayout(mContext);
        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(0, 0, px, 0);
        layout_btn_left.setLayoutParams(params);
        positiveBtn = new ImageView(mContext);
        RelativeLayout.LayoutParams image_success_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        image_success_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        image_success_params.addRule(RelativeLayout.CENTER_VERTICAL);
        positiveBtn.setLayoutParams(image_success_params);
        positiveBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        ContextCompat.getDrawable(mContext, R.drawable.success);
        positiveBtn.setImageResource(R.drawable.success_64dp);
        layout_btn_left.addView(positiveBtn);

        RelativeLayout layout_btn_right = new RelativeLayout(mContext);
        params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(px, 0, 0, 0);
        layout_btn_right.setLayoutParams(params);
        negativeBtn = new ImageView(mContext);
        RelativeLayout.LayoutParams image_error_params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        image_error_params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        image_error_params.addRule(RelativeLayout.CENTER_VERTICAL);
        negativeBtn.setLayoutParams(image_error_params);
        negativeBtn.setScaleType(ImageView.ScaleType.FIT_CENTER);
//        ContextCompat.getDrawable(mContext, R.drawable.success);
        negativeBtn.setImageResource(R.drawable.error);
        layout_btn_right.addView(negativeBtn);


        buttons_parent_layout.addView(layout_btn_left);
        buttons_parent_layout.addView(layout_btn_right);

        cardview_parent_layout.addView(buttons_parent_layout);


    }

    public Card(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.Card,
                0, 0);
        try {
            backgroundColor = a.getColor(R.styleable.Card_card_background, 0);
            primaryColor = a.getColor(R.styleable.Card_primary_color, 0);
            back_color = a.getColor(R.styleable.Card_back_color, 0);
            positiveBtnResource = a.getResourceId(R.styleable.Card_pos_button_sourse, 0);
            negativeBtnResource = a.getResourceId(R.styleable.Card_negative_button_sourse, 0);

        } finally {
            a.recycle();
        }
        mContext = context;
        cardView = this;


        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        sizeWindow = new Point();
        display.getSize(sizeWindow);
        cardView.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xStartCard = cardView.getX();
                yStartCard = cardView.getY();
                xCurrent = motionEvent.getX();
                yCurrent = motionEvent.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                cardView.setX(cardView.getX() + (motionEvent.getX() - xCurrent));
                cardView.setY(cardView.getY() + (motionEvent.getY() - yCurrent));
//                Toast.makeText(getContext(), String.valueOf(motionEvent.getX()), Toast.LENGTH_SHORT).show();
                break;
            case MotionEvent.ACTION_UP:
                if ((motionEvent.getEventTime() - motionEvent.getDownTime() < NOT_MOVE_TIME_VALUE
                        && motionEvent.getEventTime() - motionEvent.getDownTime() < NOT_MOVE_TIME_VALUE)
                        || (Math.abs(xStartCard - cardView.getX()) < NOT_MOVE_DISTANCE_VALUE &&
                        Math.abs(yStartCard - cardView.getY()) < NOT_MOVE_DISTANCE_VALUE)) {
//                     when touch on card
                    onCardClicked(current_word);
                } else if ((cardView.getX() + cardView.getWidth()) < RIGHT_DISABLE_COEFFICIENT * sizeWindow.x ){
                    onSwipedLeft();
                }
                else if(cardView.getX() > LEFT_DISABLE_COEFFICIENT * sizeWindow.x) {
                    // when swipe card
                    onSwipedRight(); //
                }
                cardView.setX(xStartCard);
                cardView.setY(yStartCard);
                break;
        }
        return true;
    }

    public void onSwiped() {
        if (DefaultLang) {
            isEnglish = true;
        } else {
            isEnglish = false;
        }
        AnimatorHelper.changeView(mContext, cardView);
        current_word = (WordInterface) adapter.getRandomItem();
        if (current_word != null) showWordOnCard(current_word);
    }

    public void setEnglishWord(WordInterface word) {
        wordTextView.setText(word.getEnWord());
        transcriptionTextView.setText("[" + word.getTranscription() + "]");
    }

    public void setRussianWord(WordInterface word) {
        AbstractList list = word.getTranslation();
        String translations = new String();
        for (Object obj : list) {
            translations += obj.toString() + "\n";
        }
        if (translations != null && translations.length() > 0 ) {
            translations = translations.substring(0, translations.length() - 1);
        }
        wordTextView.setText(translations);
        transcriptionTextView.setText("");
    }

    public void showWordOnCard(WordInterface word) {
        if (DefaultLang) setEnglishWord(word);
        else
            setRussianWord(word);
    }

    @Override
    public void onSwipedLeft() {
        onSwiped();
    }

    @Override
    public void onSwipedRight() {
        onSwiped();
    }


    @Override
    public void onCardClicked(WordInterface cur_item) {
        isEnglish = isEnglish ? false : true;
        AnimatorHelper.flipView(mContext, cardView, current_word, this);

    }

    @Override
    public void onFlipAnimEnded(WordInterface item) {
        if (isEnglish) {
            setEnglishWord(item);

        } else {
            setRussianWord(item);
        }
    }

    @Override
    public void onDataSetChanged() {
        current_word = (WordInterface) adapter.getRandomItem();
        if (current_word != null) showWordOnCard(current_word);
    }

    @Override
    public void onPositiveButtonClicked() {
        onSwipedLeft();
    }

    @Override
    public void onNegativeButtonClicked() {
        onSwipedRight();
    }

}