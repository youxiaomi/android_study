package com.example.you.testapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
  private Animator mCurrentAnimator;

  private int mShortAnimationDuration = 5000;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    final View thumb1View = findViewById(R.id.thumb_button_1);
    thumb1View.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        thumb1View.setVisibility(View.GONE);
        zoomImageFromThumb(thumb1View, R.drawable.eye);
      }
    });


  }


  private void zoomImageFromThumb(final View thumbView, int imageResId) {

    final ImageView expandedImageView = (ImageView) findViewById(
      R.id.expanded_image);
    expandedImageView.setImageResource(imageResId);


    final Rect startBounds = new Rect();
    final Rect finalBounds = new Rect();
    final Point globalOffset = new Point();

    thumbView.getGlobalVisibleRect(startBounds);
    findViewById(R.id.container)
      .getGlobalVisibleRect(finalBounds, globalOffset);
    startBounds.offset(-globalOffset.x, -globalOffset.y);
    finalBounds.offset(-globalOffset.x, -globalOffset.y);

    float startScale;
    if ((float) finalBounds.width() / finalBounds.height()
      > (float) startBounds.width() / startBounds.height()) {
      startScale = (float) startBounds.height() / finalBounds.height();
    } else {
      startScale = (float) startBounds.width() / finalBounds.width();
    }
    expandedImageView.setVisibility(View.VISIBLE);

    final float startScaleFinal = startScale;

    minScale(expandedImageView, startScaleFinal ,"max");





  }
  private void minScale(final View expandedImageView,final float startScaleFinal,final String type){
    AnimatorSet set = new AnimatorSet();
    if(type == "max"){
      set
        .play(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScaleFinal, 1f))
        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal, 1f));
    }else{
      set
        .play(ObjectAnimator
          .ofFloat(expandedImageView, View.SCALE_X, startScaleFinal))
        .with(ObjectAnimator
          .ofFloat(expandedImageView, View.SCALE_Y, startScaleFinal));
    }
    set.setDuration(mShortAnimationDuration);
    set.setInterpolator(new DecelerateInterpolator());
    set.addListener(new AnimatorListenerAdapter() {
      @Override
      public void onAnimationEnd(Animator animation) {
        mCurrentAnimator = null;
        if(type == "max"){
          minScale(expandedImageView, startScaleFinal ,"min");
        }else{
          minScale(expandedImageView, startScaleFinal ,"max");
        }
      }

      @Override
      public void onAnimationCancel(Animator animation) {
        mCurrentAnimator = null;
      }
    });
    set.start();
    mCurrentAnimator = set;
  }
}
