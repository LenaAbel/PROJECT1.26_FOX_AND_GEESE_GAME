package gamifier.model.animation;

import gamifier.model.Model;

import java.util.ArrayList;
import java.util.List;

public class FaceAnimation extends Animation {
    // the list of possible index of faces that are used during this animation
    protected List<Integer> faceIndexes;
    // waits : waiting time between a change in look. Will be rounded to the closest multiples of frameGap
    protected int waitTime;


    public FaceAnimation(Model model, List<Integer> faceindexes, int waitTime) {
        super(model, AnimationTypes.getType("look/simple"));
        this.faceIndexes = faceindexes;
        if (waitTime < frameGap) waitTime = frameGap;
        waitTime = (waitTime/frameGap)*frameGap;
        this.waitTime = waitTime;
        // WARNING : the dev will have to explicitly call computeSteps()
    }

    public FaceAnimation(Model model, int nbFaces, int waitTime) {
        this(model, expandIntegerList(nbFaces), waitTime);
    }

    protected static List<Integer> expandIntegerList(int max) {
        List<Integer> indexes = new ArrayList<>();
        for(int i = 0; i< max; i++) indexes.add(i);
        return indexes;
    }

    @Override
    public void computeSteps() {
        for (int val : faceIndexes) {
            for (int j = 0; j < waitTime / frameGap; j++) {
                AnimationStep step = new AnimationStep();
                step.addData(val);
                steps.add(step);
            }
        }
    }
}
