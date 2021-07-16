package com.example.imagesearch.ui.extension

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.imagesearch.R

fun ImageView.loadImageUrl(url: String) {
    Glide.with(this)
        .asBitmap()
        .placeholder(R.drawable.image_placeholder)
        .centerCrop()
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                this@loadImageUrl.setImageBitmap(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                this@loadImageUrl.setImageDrawable(placeholder)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                this@loadImageUrl.setImageResource(R.drawable.image_load_error)
            }
        })
}