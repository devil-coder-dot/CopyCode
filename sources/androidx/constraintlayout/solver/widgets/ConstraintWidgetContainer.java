package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.Metrics;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type;
import androidx.constraintlayout.solver.widgets.ConstraintWidget.DimensionBehaviour;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstraintWidgetContainer extends WidgetContainer {
    private static final boolean DEBUG = false;
    static final boolean DEBUG_GRAPH = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final int MAX_ITERATIONS = 8;
    private static final boolean USE_SNAPSHOT = true;
    int mDebugSolverPassCount = 0;
    public boolean mGroupsWrapOptimized = false;
    private boolean mHeightMeasuredTooSmall = false;
    ChainHead[] mHorizontalChainsArray = new ChainHead[4];
    int mHorizontalChainsSize = 0;
    public boolean mHorizontalWrapOptimized = false;
    private boolean mIsRtl = false;
    private int mOptimizationLevel = 7;
    int mPaddingBottom;
    int mPaddingLeft;
    int mPaddingRight;
    int mPaddingTop;
    public boolean mSkipSolver = false;
    private Snapshot mSnapshot;
    protected LinearSystem mSystem = new LinearSystem();
    ChainHead[] mVerticalChainsArray = new ChainHead[4];
    int mVerticalChainsSize = 0;
    public boolean mVerticalWrapOptimized = false;
    public List<ConstraintWidgetGroup> mWidgetGroups = new ArrayList();
    private boolean mWidthMeasuredTooSmall = false;
    public int mWrapFixedHeight = 0;
    public int mWrapFixedWidth = 0;

    public String getType() {
        return "ConstraintLayout";
    }

    public boolean handlesInternalConstraints() {
        return false;
    }

    public void fillMetrics(Metrics metrics) {
        this.mSystem.fillMetrics(metrics);
    }

    public ConstraintWidgetContainer() {
    }

    public ConstraintWidgetContainer(int i, int i2, int i3, int i4) {
        super(i, i2, i3, i4);
    }

    public ConstraintWidgetContainer(int i, int i2) {
        super(i, i2);
    }

    public void setOptimizationLevel(int i) {
        this.mOptimizationLevel = i;
    }

    public int getOptimizationLevel() {
        return this.mOptimizationLevel;
    }

    public boolean optimizeFor(int i) {
        if ((this.mOptimizationLevel & i) == i) {
            return USE_SNAPSHOT;
        }
        return false;
    }

    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingRight = 0;
        this.mPaddingTop = 0;
        this.mPaddingBottom = 0;
        this.mWidgetGroups.clear();
        this.mSkipSolver = false;
        super.reset();
    }

    public boolean isWidthMeasuredTooSmall() {
        return this.mWidthMeasuredTooSmall;
    }

    public boolean isHeightMeasuredTooSmall() {
        return this.mHeightMeasuredTooSmall;
    }

    public boolean addChildrenToSolver(LinearSystem linearSystem) {
        addToSolver(linearSystem);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            if (constraintWidget instanceof ConstraintWidgetContainer) {
                DimensionBehaviour dimensionBehaviour = constraintWidget.mListDimensionBehaviors[0];
                DimensionBehaviour dimensionBehaviour2 = constraintWidget.mListDimensionBehaviors[1];
                if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                if (dimensionBehaviour2 == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(DimensionBehaviour.FIXED);
                }
                constraintWidget.addToSolver(linearSystem);
                if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setHorizontalDimensionBehaviour(dimensionBehaviour);
                }
                if (dimensionBehaviour2 == DimensionBehaviour.WRAP_CONTENT) {
                    constraintWidget.setVerticalDimensionBehaviour(dimensionBehaviour2);
                }
            } else {
                Optimizer.checkMatchParent(this, linearSystem, constraintWidget);
                constraintWidget.addToSolver(linearSystem);
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 1);
        }
        return USE_SNAPSHOT;
    }

    public void updateChildrenFromSolver(LinearSystem linearSystem, boolean[] zArr) {
        zArr[2] = false;
        updateFromSolver(linearSystem);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            constraintWidget.updateFromSolver(linearSystem);
            if (constraintWidget.mListDimensionBehaviors[0] == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getWidth() < constraintWidget.getWrapWidth()) {
                zArr[2] = USE_SNAPSHOT;
            }
            if (constraintWidget.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget.getHeight() < constraintWidget.getWrapHeight()) {
                zArr[2] = USE_SNAPSHOT;
            }
        }
    }

    public void setPadding(int i, int i2, int i3, int i4) {
        this.mPaddingLeft = i;
        this.mPaddingTop = i2;
        this.mPaddingRight = i3;
        this.mPaddingBottom = i4;
    }

    public void setRtl(boolean z) {
        this.mIsRtl = z;
    }

    public boolean isRtl() {
        return this.mIsRtl;
    }

    public void analyze(int i) {
        super.analyze(i);
        int size = this.mChildren.size();
        for (int i2 = 0; i2 < size; i2++) {
            ((ConstraintWidget) this.mChildren.get(i2)).analyze(i);
        }
    }

    /* JADX WARNING: type inference failed for: r8v17, types: [boolean] */
    /* JADX WARNING: type inference failed for: r8v21 */
    /* JADX WARNING: type inference failed for: r8v22 */
    /* JADX WARNING: type inference failed for: r8v54 */
    /* JADX WARNING: type inference failed for: r8v55 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r8v17, types: [boolean]
  assigns: []
  uses: [?[int, short, byte, char], boolean]
  mth insns count: 345
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x0265  */
    /* JADX WARNING: Removed duplicated region for block: B:109:0x0282  */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x028f  */
    /* JADX WARNING: Removed duplicated region for block: B:112:0x0294  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0188  */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x0191  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x01dc  */
    /* JADX WARNING: Unknown variable types count: 3 */
    public void layout() {
        int i;
        int i2;
        char c;
        int i3;
        boolean z;
        int max;
        int max2;
        boolean z2;
        ? r8;
        int i4 = this.mX;
        int i5 = this.mY;
        int max3 = Math.max(0, getWidth());
        int max4 = Math.max(0, getHeight());
        this.mWidthMeasuredTooSmall = false;
        this.mHeightMeasuredTooSmall = false;
        if (this.mParent != null) {
            if (this.mSnapshot == null) {
                this.mSnapshot = new Snapshot(this);
            }
            this.mSnapshot.updateFrom(this);
            setX(this.mPaddingLeft);
            setY(this.mPaddingTop);
            resetAnchors();
            resetSolverVariables(this.mSystem.getCache());
        } else {
            this.mX = 0;
            this.mY = 0;
        }
        int i6 = 32;
        if (this.mOptimizationLevel != 0) {
            if (!optimizeFor(8)) {
                optimizeReset();
            }
            if (!optimizeFor(32)) {
                optimize();
            }
            this.mSystem.graphOptimizer = USE_SNAPSHOT;
        } else {
            this.mSystem.graphOptimizer = false;
        }
        DimensionBehaviour dimensionBehaviour = this.mListDimensionBehaviors[1];
        DimensionBehaviour dimensionBehaviour2 = this.mListDimensionBehaviors[0];
        resetChains();
        if (this.mWidgetGroups.size() == 0) {
            this.mWidgetGroups.clear();
            this.mWidgetGroups.add(0, new ConstraintWidgetGroup(this.mChildren));
        }
        int size = this.mWidgetGroups.size();
        ArrayList arrayList = this.mChildren;
        boolean z3 = (getHorizontalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT || getVerticalDimensionBehaviour() == DimensionBehaviour.WRAP_CONTENT) ? USE_SNAPSHOT : false;
        boolean z4 = false;
        int i7 = 0;
        while (i7 < size && !this.mSkipSolver) {
            if (((ConstraintWidgetGroup) this.mWidgetGroups.get(i7)).mSkipSolver) {
                i = size;
            } else {
                if (optimizeFor(i6)) {
                    if (getHorizontalDimensionBehaviour() == DimensionBehaviour.FIXED && getVerticalDimensionBehaviour() == DimensionBehaviour.FIXED) {
                        this.mChildren = (ArrayList) ((ConstraintWidgetGroup) this.mWidgetGroups.get(i7)).getWidgetsToSolve();
                    } else {
                        this.mChildren = (ArrayList) ((ConstraintWidgetGroup) this.mWidgetGroups.get(i7)).mConstrainedGroup;
                    }
                }
                resetChains();
                int size2 = this.mChildren.size();
                for (int i8 = 0; i8 < size2; i8++) {
                    ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i8);
                    if (constraintWidget instanceof WidgetContainer) {
                        ((WidgetContainer) constraintWidget).layout();
                    }
                }
                boolean z5 = z4;
                int i9 = 0;
                boolean z6 = USE_SNAPSHOT;
                while (z6) {
                    boolean z7 = z5;
                    int i10 = i9 + 1;
                    try {
                        this.mSystem.reset();
                        resetChains();
                        createObjectVariables(this.mSystem);
                        int i11 = 0;
                        while (i11 < size2) {
                            boolean z8 = z6;
                            try {
                                ((ConstraintWidget) this.mChildren.get(i11)).createObjectVariables(this.mSystem);
                                i11++;
                                z6 = z8;
                            } catch (Exception e) {
                                e = e;
                                z6 = z8;
                                e.printStackTrace();
                                PrintStream printStream = System.out;
                                boolean z9 = z6;
                                StringBuilder sb = new StringBuilder();
                                i2 = size;
                                sb.append("EXCEPTION : ");
                                sb.append(e);
                                printStream.println(sb.toString());
                                z6 = z9;
                                if (z6) {
                                }
                                c = 2;
                                if (z3) {
                                }
                                i3 = i10;
                                z = false;
                                max = Math.max(this.mMinWidth, getWidth());
                                if (max > getWidth()) {
                                }
                                max2 = Math.max(this.mMinHeight, getHeight());
                                if (max2 <= getHeight()) {
                                }
                                if (!z2) {
                                }
                                z6 = z;
                                z5 = z2;
                                i9 = i3;
                                size = i2;
                            }
                        }
                        boolean z10 = z6;
                        z6 = addChildrenToSolver(this.mSystem);
                        if (z6) {
                            try {
                                this.mSystem.minimize();
                            } catch (Exception e2) {
                                e = e2;
                            }
                        }
                        i2 = size;
                    } catch (Exception e3) {
                        e = e3;
                        boolean z11 = z6;
                        e.printStackTrace();
                        PrintStream printStream2 = System.out;
                        boolean z92 = z6;
                        StringBuilder sb2 = new StringBuilder();
                        i2 = size;
                        sb2.append("EXCEPTION : ");
                        sb2.append(e);
                        printStream2.println(sb2.toString());
                        z6 = z92;
                        if (z6) {
                        }
                        c = 2;
                        if (z3) {
                        }
                        i3 = i10;
                        z = false;
                        max = Math.max(this.mMinWidth, getWidth());
                        if (max > getWidth()) {
                        }
                        max2 = Math.max(this.mMinHeight, getHeight());
                        if (max2 <= getHeight()) {
                        }
                        if (!z2) {
                        }
                        z6 = z;
                        z5 = z2;
                        i9 = i3;
                        size = i2;
                    }
                    if (z6) {
                        updateChildrenFromSolver(this.mSystem, Optimizer.flags);
                    } else {
                        updateFromSolver(this.mSystem);
                        int i12 = 0;
                        while (true) {
                            if (i12 >= size2) {
                                break;
                            }
                            ConstraintWidget constraintWidget2 = (ConstraintWidget) this.mChildren.get(i12);
                            if (constraintWidget2.mListDimensionBehaviors[0] != DimensionBehaviour.MATCH_CONSTRAINT || constraintWidget2.getWidth() >= constraintWidget2.getWrapWidth()) {
                                if (constraintWidget2.mListDimensionBehaviors[1] == DimensionBehaviour.MATCH_CONSTRAINT && constraintWidget2.getHeight() < constraintWidget2.getWrapHeight()) {
                                    c = 2;
                                    Optimizer.flags[2] = USE_SNAPSHOT;
                                    break;
                                }
                                i12++;
                            } else {
                                Optimizer.flags[2] = USE_SNAPSHOT;
                                break;
                            }
                        }
                        if (z3 || i10 >= 8 || !Optimizer.flags[c]) {
                            i3 = i10;
                            z = false;
                        } else {
                            int i13 = 0;
                            int i14 = 0;
                            int i15 = 0;
                            while (i13 < size2) {
                                ConstraintWidget constraintWidget3 = (ConstraintWidget) this.mChildren.get(i13);
                                int i16 = i10;
                                i14 = Math.max(i14, constraintWidget3.mX + constraintWidget3.getWidth());
                                i15 = Math.max(i15, constraintWidget3.mY + constraintWidget3.getHeight());
                                i13++;
                                i10 = i16;
                            }
                            i3 = i10;
                            int max5 = Math.max(this.mMinWidth, i14);
                            int max6 = Math.max(this.mMinHeight, i15);
                            if (dimensionBehaviour2 != DimensionBehaviour.WRAP_CONTENT || getWidth() >= max5) {
                                z = false;
                            } else {
                                setWidth(max5);
                                this.mListDimensionBehaviors[0] = DimensionBehaviour.WRAP_CONTENT;
                                z = USE_SNAPSHOT;
                                z7 = USE_SNAPSHOT;
                            }
                            if (dimensionBehaviour == DimensionBehaviour.WRAP_CONTENT && getHeight() < max6) {
                                setHeight(max6);
                                this.mListDimensionBehaviors[1] = DimensionBehaviour.WRAP_CONTENT;
                                z = USE_SNAPSHOT;
                                z7 = USE_SNAPSHOT;
                            }
                        }
                        max = Math.max(this.mMinWidth, getWidth());
                        if (max > getWidth()) {
                            setWidth(max);
                            this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
                            z = USE_SNAPSHOT;
                            z7 = USE_SNAPSHOT;
                        }
                        max2 = Math.max(this.mMinHeight, getHeight());
                        if (max2 <= getHeight()) {
                            setHeight(max2);
                            this.mListDimensionBehaviors[1] = DimensionBehaviour.FIXED;
                            z = USE_SNAPSHOT;
                            z2 = USE_SNAPSHOT;
                            r8 = 1;
                        } else {
                            z2 = z7;
                            r8 = 1;
                        }
                        if (!z2) {
                            if (this.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT && max3 > 0 && getWidth() > max3) {
                                this.mWidthMeasuredTooSmall = r8;
                                this.mListDimensionBehaviors[0] = DimensionBehaviour.FIXED;
                                setWidth(max3);
                                z = USE_SNAPSHOT;
                                z2 = USE_SNAPSHOT;
                            }
                            if (this.mListDimensionBehaviors[r8] == DimensionBehaviour.WRAP_CONTENT && max4 > 0 && getHeight() > max4) {
                                this.mHeightMeasuredTooSmall = r8;
                                this.mListDimensionBehaviors[r8] = DimensionBehaviour.FIXED;
                                setHeight(max4);
                                z5 = USE_SNAPSHOT;
                                z6 = USE_SNAPSHOT;
                                i9 = i3;
                                size = i2;
                            }
                        }
                        z6 = z;
                        z5 = z2;
                        i9 = i3;
                        size = i2;
                    }
                    c = 2;
                    if (z3) {
                    }
                    i3 = i10;
                    z = false;
                    max = Math.max(this.mMinWidth, getWidth());
                    if (max > getWidth()) {
                    }
                    max2 = Math.max(this.mMinHeight, getHeight());
                    if (max2 <= getHeight()) {
                    }
                    if (!z2) {
                    }
                    z6 = z;
                    z5 = z2;
                    i9 = i3;
                    size = i2;
                }
                boolean z12 = z5;
                i = size;
                ((ConstraintWidgetGroup) this.mWidgetGroups.get(i7)).updateUnresolvedWidgets();
                z4 = z12;
            }
            i7++;
            size = i;
            i6 = 32;
        }
        this.mChildren = arrayList;
        if (this.mParent != null) {
            int max7 = Math.max(this.mMinWidth, getWidth());
            int max8 = Math.max(this.mMinHeight, getHeight());
            this.mSnapshot.applyTo(this);
            setWidth(max7 + this.mPaddingLeft + this.mPaddingRight);
            setHeight(max8 + this.mPaddingTop + this.mPaddingBottom);
        } else {
            this.mX = i4;
            this.mY = i5;
        }
        if (z4) {
            this.mListDimensionBehaviors[0] = dimensionBehaviour2;
            this.mListDimensionBehaviors[1] = dimensionBehaviour;
        }
        resetSolverVariables(this.mSystem.getCache());
        if (this == getRootConstraintContainer()) {
            updateDrawPosition();
        }
    }

    public void preOptimize() {
        optimizeReset();
        analyze(this.mOptimizationLevel);
    }

    public void solveGraph() {
        ResolutionAnchor resolutionNode = getAnchor(Type.LEFT).getResolutionNode();
        ResolutionAnchor resolutionNode2 = getAnchor(Type.TOP).getResolutionNode();
        resolutionNode.resolve(null, 0.0f);
        resolutionNode2.resolve(null, 0.0f);
    }

    public void resetGraph() {
        ResolutionAnchor resolutionNode = getAnchor(Type.LEFT).getResolutionNode();
        ResolutionAnchor resolutionNode2 = getAnchor(Type.TOP).getResolutionNode();
        resolutionNode.invalidateAnchors();
        resolutionNode2.invalidateAnchors();
        resolutionNode.resolve(null, 0.0f);
        resolutionNode2.resolve(null, 0.0f);
    }

    public void optimizeForDimensions(int i, int i2) {
        if (!(this.mListDimensionBehaviors[0] == DimensionBehaviour.WRAP_CONTENT || this.mResolutionWidth == null)) {
            this.mResolutionWidth.resolve(i);
        }
        if (this.mListDimensionBehaviors[1] != DimensionBehaviour.WRAP_CONTENT && this.mResolutionHeight != null) {
            this.mResolutionHeight.resolve(i2);
        }
    }

    public void optimizeReset() {
        int size = this.mChildren.size();
        resetResolutionNodes();
        for (int i = 0; i < size; i++) {
            ((ConstraintWidget) this.mChildren.get(i)).resetResolutionNodes();
        }
    }

    public void optimize() {
        if (!optimizeFor(8)) {
            analyze(this.mOptimizationLevel);
        }
        solveGraph();
    }

    public ArrayList<Guideline> getVerticalGuidelines() {
        ArrayList<Guideline> arrayList = new ArrayList<>();
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget;
                if (guideline.getOrientation() == 1) {
                    arrayList.add(guideline);
                }
            }
        }
        return arrayList;
    }

    public ArrayList<Guideline> getHorizontalGuidelines() {
        ArrayList<Guideline> arrayList = new ArrayList<>();
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = (ConstraintWidget) this.mChildren.get(i);
            if (constraintWidget instanceof Guideline) {
                Guideline guideline = (Guideline) constraintWidget;
                if (guideline.getOrientation() == 0) {
                    arrayList.add(guideline);
                }
            }
        }
        return arrayList;
    }

    public LinearSystem getSystem() {
        return this.mSystem;
    }

    private void resetChains() {
        this.mHorizontalChainsSize = 0;
        this.mVerticalChainsSize = 0;
    }

    /* access modifiers changed from: 0000 */
    public void addChain(ConstraintWidget constraintWidget, int i) {
        if (i == 0) {
            addHorizontalChain(constraintWidget);
        } else if (i == 1) {
            addVerticalChain(constraintWidget);
        }
    }

    private void addHorizontalChain(ConstraintWidget constraintWidget) {
        int i = this.mHorizontalChainsSize + 1;
        ChainHead[] chainHeadArr = this.mHorizontalChainsArray;
        if (i >= chainHeadArr.length) {
            this.mHorizontalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
        }
        this.mHorizontalChainsArray[this.mHorizontalChainsSize] = new ChainHead(constraintWidget, 0, isRtl());
        this.mHorizontalChainsSize++;
    }

    private void addVerticalChain(ConstraintWidget constraintWidget) {
        int i = this.mVerticalChainsSize + 1;
        ChainHead[] chainHeadArr = this.mVerticalChainsArray;
        if (i >= chainHeadArr.length) {
            this.mVerticalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
        }
        this.mVerticalChainsArray[this.mVerticalChainsSize] = new ChainHead(constraintWidget, 1, isRtl());
        this.mVerticalChainsSize++;
    }

    public List<ConstraintWidgetGroup> getWidgetGroups() {
        return this.mWidgetGroups;
    }
}
