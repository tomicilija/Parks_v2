package si.parks.parks;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import com.example.LokacijaTag;



public class TagTextView extends android.support.v7.widget.AppCompatTextView {

    int padding_in_px;
    LokacijaTag tag;
    boolean guiCheckState;

    public void updateObjectState() {
        tag.getTag().setChecked(guiCheckState);
    }

    public TagTextView(Context context, LokacijaTag tag,boolean clickable) {
        super(context);
        setClickable(clickable);
        setDefaultSettings();
        this.tag = tag;
        guiCheckState = tag.getTag().isChecked();
        setCheck(guiCheckState);
        setText(tag.getTag().getIme());
    }
    public void setDefaultSettings() {
        this.setTextColor(Color.WHITE);
        setTypeface(Typeface.DEFAULT_BOLD);
        int padding_in_dp = 6;  // 6 dps
        final float scale = getResources().getDisplayMetrics().density;
        padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        setOnClick();

    }

    public void setCheck(boolean mc){
        guiCheckState = mc;
        setState();
    }
    private void setState() {
        if(guiCheckState) {
            this.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
            setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
            setElevation(padding_in_px);
        } else {
            this.setBackground(getResources().getDrawable(R.drawable.rounded_corner_gray));
            setPadding(padding_in_px,padding_in_px,padding_in_px,padding_in_px);
            setElevation(0);
        }
    }
    public LokacijaTag getTag() {
        return tag;
    }
    public TagTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
    }

    public TagTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setBackground(getResources().getDrawable(R.drawable.rounded_corner));
    }

    private void setOnClick() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setCheck(!guiCheckState);
            }
        });
    }
}