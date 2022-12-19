package gamifier.view;

import gamifier.model.GameElement;
import gamifier.model.SpriteElement;

public abstract class SpriteLook extends ElementLook {

    public SpriteLook(GameElement element) {
        super(element);
    }

    @Override
    public void onChange() {
        updateFace();
    }

    public abstract void updateFace();
}
