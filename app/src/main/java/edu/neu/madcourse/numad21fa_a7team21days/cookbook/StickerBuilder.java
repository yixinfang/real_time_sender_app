package edu.neu.madcourse.numad21fa_a7team21days.cookbook;

import android.widget.ImageButton;

public class StickerBuilder {
    private Sticker sticker;
    private ImageButton imageButton;

    public StickerBuilder(Sticker sticker, ImageButton imageButton) {
        this.sticker = sticker;
        this.imageButton = imageButton;
    }

    public Sticker getSticker() {
        return sticker;
    }

    public void setSticker(Sticker sticker) {
        this.sticker = sticker;
    }

    public ImageButton getImageButton() {
        return imageButton;
    }

    public void setImageButton(ImageButton imageButton) {
        this.imageButton = imageButton;
    }
}
