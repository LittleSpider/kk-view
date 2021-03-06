package cn.kkmofang.view;

import android.os.Handler;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import cn.kkmofang.view.value.Color;
import cn.kkmofang.view.value.Font;
import cn.kkmofang.view.value.Pixel;
import cn.kkmofang.view.value.TextAlign;

/**
 * Created by zhanghailong on 2018/1/20.
 */

public class TextElement extends ViewElement{

    public final Pixel lineSpacing = new Pixel();
    public final Pixel letterSpacing = new Pixel();
    private final Text _text = new Text();

    private final Handler _handler;

    public Text text() {

        if(_text.isNeedDisplay()) {

            _text.string.clear();

            Element p = firstChild();

            if(p == null) {
                String v = get("#text");
                if (v != null)
                    _text.string.append(v);
            } else {
                while (p != null) {
                    if (p instanceof SpanElement) {
                        SpannableString ss = ((SpanElement) p).obtainContent();
                        if (ss != null)
                            _text.string.append(ss);
                    }

                    if (p instanceof ImgElement) {
                        SpannableString ss = ((ImgElement) p).obtainContent();
                        if (ss != null) {
                            _text.string.append(ss);
                        }
                    }
                    p = p.nextSibling();
                }
            }
        }

        return _text;
    }


    private static final Layout TextLayout = new Layout(){
        @Override
        public void layout(ViewElement element) {

            float width = element.width();
            float height = element.height();


            TextElement e = (TextElement) element;

            if(width == Pixel.Auto || height == Pixel.Auto) {

                float paddingLeft = element.padding.left.floatValue(width,0);
                float paddingRight = element.padding.right.floatValue(width,0);

                float maxWidth = width;
                float maxHeight = height;


                if(width != Pixel.Auto) {
                    maxWidth = width - paddingLeft - paddingRight;
                }

                e._text.setMaxWidth((int) maxWidth);

                if(width == Pixel.Auto) {
                    width = e.text().width();
                }

                if(height == Pixel.Auto) {
                    height = e.text().height();
                    if(height > maxHeight) {
                        height = maxHeight;
                    }
                }

                element.setContentSize(width, height);

            } else {
                e._text.setMaxWidth((int) width);
                element.setContentSize(width,height);
            }

        }
    };

    public TextElement() {
        super();
        _handler = new Handler() ;
        set("#view", TextView.class.getName());
        setLayout(TextLayout);
    }

    public void setView(View v) {
        super.setView(v);
        if(v != null) {
            setNeedDisplay();
        }
    }

    private boolean _displaying = false;

    public void setNeedDisplay() {

        _text.setNeedDisplay();

        if(view() == null) {
            return;
        }

        if(_displaying) {
            return;
        }

        _displaying = true;

        _handler.post(new Runnable() {
            @Override
            public void run() {
                TextView v = (TextView) view();
                if(v != null) {
                    v.setText(text());
                }
                _displaying = false;
            }
        });

    }

    @Override
    public void changedKey(String key) {
        super.changedKey(key);
        if("#text".equals(key)) {
            setNeedDisplay();
        } else if("font".equals(key)) {
            Font.valueOf(get(key),_text.paint);
            setNeedDisplay();
        } else if("color".equals(key)) {
            int v = Color.valueOf(get(key),0xff000000);
            _text.paint.setColor(v);
            _text.paint.setAlpha(0x0ff & (v >> 24));
            setNeedDisplay();
        } else if("line-spacing".equals(key)) {
            lineSpacing.set(get(key));
            _text.setLineSpacing((int) lineSpacing.floatValue(0,0));
            setNeedDisplay();
        } else if("letter-spacing".equals(key)) {
            letterSpacing.set(get(key));
            _text.setLineSpacing((int) lineSpacing.floatValue(0,0));
            setNeedDisplay();
        } else if("text-align".equals(key)) {
            String v = get(key);
            _text.setTextAlign(TextAlign.valueOfString(v));
            setNeedDisplay();
        }

    }

    @Override
    protected void onLayout(View view) {
        setNeedDisplay();
        super.onLayout(view);
    }

}
