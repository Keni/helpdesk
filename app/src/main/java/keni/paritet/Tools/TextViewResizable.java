package keni.paritet.Tools;

import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

/**
 * Created by Keni on 12.12.2016.
 */

public class TextViewResizable
{
    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0,
                            lineEndIndex - expandText.length() + 1)
                            + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(tv.getText()
                                            .toString(), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0,
                            lineEndIndex - expandText.length() + 1)
                            + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(tv.getText()
                                            .toString(), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(
                            tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex)
                            + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(tv.getText()
                                            .toString(), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(
            final String strSpanned, final TextView tv, final int maxLine,
            final String spanableText, final boolean viewMore) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (strSpanned.contains(spanableText)) {
            ssb.setSpan(
                    new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {

                            if (viewMore) {
                                tv.setLayoutParams(tv.getLayoutParams());
                                tv.setText(tv.getTag().toString(),
                                        TextView.BufferType.SPANNABLE);
                                tv.invalidate();
                                makeTextViewResizable(tv, -3, "скрыть...",
                                        false);
                                tv.setTextColor(Color.BLACK);
                            } else {
                                tv.setLayoutParams(tv.getLayoutParams());
                                tv.setText(tv.getTag().toString(),
                                        TextView.BufferType.SPANNABLE);
                                tv.invalidate();
                                makeTextViewResizable(tv, 3, "показать весь текст...",
                                        true);
                                tv.setTextColor(Color.BLACK);
                            }

                        }
                    }, strSpanned.indexOf(spanableText),
                    strSpanned.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;
    }
}
