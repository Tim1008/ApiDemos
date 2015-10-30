/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.apis.graphics;

import com.example.android.apis.R;

import android.content.Context;
import android.graphics.*;
import android.os.Bundle;
import android.view.View;

public class ColorMatrixSample extends GraphicsActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new SampleView(this));
    }

    private static class SampleView extends View {
        private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//没有锯齿
        private Bitmap mBitmap;
        private float mAngle;

        public SampleView(Context context) {
            super(context);

            mBitmap = BitmapFactory.decodeResource(context.getResources(),
                                                   R.drawable.balloons);
        }

        private static void setTranslate(ColorMatrix cm, float dr, float dg,
                                         float db, float da) {
            cm.set(new float[] {
                   2, 0, 0, 0, dr,
                   0, 2, 0, 0, dg,
                   0, 0, 2, 0, db,
                   0, 0, 0, 1, da });
        }
        /**
         * 变换+移动
         * @param cm
         * @param contrast
         */
        private static void setContrast(ColorMatrix cm, float contrast) {
            float scale = contrast + 1.f;
               float translate = (-.5f * scale + .5f) * 255.f;
               //ColorMatrix是一个5*4的矩阵，set方法是给该矩阵赋值。
               //当用到(r,g,b,a)变换时，r' = scale*r+translate; b' = scale*b + translate;g = g'*scale+translate,a'=a;
            cm.set(new float[] {
                   scale, 0, 0, 0, translate,
                   0, scale, 0, 0, translate,
                   0, 0, scale, 0, translate,
                   0, 0, 0, 1, 0 });
        }
        /**
         * 仅移动
         * @param cm
         * @param contrast
         */
        private static void setContrastTranslateOnly(ColorMatrix cm, float contrast) {
            float scale = contrast + 1.f;
               float translate = (-.5f * scale + .5f) * 255.f;
            cm.set(new float[] {
                   1, 0, 0, 0, translate,
                   0, 1, 0, 0, translate,
                   0, 0, 1, 0, translate,
                   0, 0, 0, 1, 0 });
        }
        /**
         * 仅变换
         * @param cm
         * @param contrast
         */
        private static void setContrastScaleOnly(ColorMatrix cm, float contrast) {
            float scale = contrast + 1.f;
               float translate = (-.5f * scale + .5f) * 255.f;
            cm.set(new float[] {
                   scale, 0, 0, 0, 0,
                   0, scale, 0, 0, 0,
                   0, 0, scale, 0, 0,
                   0, 0, 0, 1, 0 });
        }

        @Override protected void onDraw(Canvas canvas) {
            Paint paint = mPaint;
            float x = 20;
            float y = 20;

            canvas.drawColor(Color.WHITE);//背景色白色

            paint.setColorFilter(null);
            canvas.drawBitmap(mBitmap, x, y, paint);//在（x,y）处绘制一个mBitmap

            ColorMatrix cm = new ColorMatrix();

            mAngle += 2;
            if (mAngle > 180) {
                mAngle = 0;
            }

            //convert our animated angle [-180...180] to a contrast value of [-1..1]
            float contrast = mAngle / 180.f;
            //scale+translate
            setContrast(cm, contrast);
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            canvas.drawBitmap(mBitmap, x + mBitmap.getWidth() + 10, y, paint);
            //scale
            setContrastScaleOnly(cm, contrast);
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            canvas.drawBitmap(mBitmap, x, y + mBitmap.getHeight() + 10, paint);
            //translate
            setContrastTranslateOnly(cm, contrast);
            paint.setColorFilter(new ColorMatrixColorFilter(cm));
            canvas.drawBitmap(mBitmap, x, y + 2*(mBitmap.getHeight() + 10),
                              paint);

            invalidate();//导致重绘
        }
    }
}

