package gamifier.model.animation;

import gamifier.model.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CyclicFaceAnimation extends FaceAnimation {
    // number of cycles, each cycle contains all possibilities of numbers from 0 to nbLook-1
    protected int nbCycles;
    // is each sequence must be randomly shuffled
    protected boolean randomSequence;

    public CyclicFaceAnimation(Model model, List<Integer> faceIndexes, int nbCycles, int waitTime, boolean randomSequence) {
        super(model, faceIndexes, waitTime);
        this.randomSequence = randomSequence;
        if (randomSequence) {
            type = AnimationTypes.getType("look/random");
        } else {
            type = AnimationTypes.getType("look/sequence");
        }
        this.nbCycles = nbCycles;
        // WARNING : the dev will have to explicitly call computeSteps()
    }

    public CyclicFaceAnimation(Model model, int nbFaces, int nbCycles, int waitTime, boolean randomSequence) {
        this(model, expandIntegerList(nbFaces), nbCycles, waitTime, randomSequence);
    }

    @Override
    public void computeSteps() {
        List<Integer> num = new ArrayList<>();
        num.addAll(faceIndexes);
        for (int i = 0; i < nbCycles; i++) {
            if (randomSequence) {
                Collections.shuffle(num);
            }
            for (int val : num) {
                for (int k = 0; k < waitTime / frameGap; k++) {
                    AnimationStep step = new AnimationStep();
                    step.addData(val);
                    steps.add(step);
                }
            }
        }
    }
}
