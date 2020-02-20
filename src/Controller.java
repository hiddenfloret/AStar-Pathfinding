import java.util.Random;

import javax.swing.JRadioButton;

public class Controller
{
    private Field[][] fields;
    private Field startField, destinationField;

    private boolean isInitialized;
    private boolean existsStartField, existsDestinationField;
    private boolean isAnimation, isSmallSteps;
    private boolean isAnimationDone;
    private boolean hasAnimationStarted;
    private boolean hasSmallStepsStarted;
    private boolean areFieldsDeleted;
    private boolean hasStarted;

    private int timeAnimation;

    private Pathfinder pathfinder;

    private Thread threadPathfinder;

    public void initFields(int numOfFields)
    {
        int fieldWidth;

        fields = new Field[numOfFields][numOfFields];

        int length = GUI.LENGTH;
        fieldWidth = length / numOfFields;

        for (int i = 0; i < length; i += fieldWidth)
        {
            for (int j = 0; j < length; j += fieldWidth)
            {
                fields[i / fieldWidth][j / fieldWidth] = new Field(i, j,
                        fieldWidth, false);
            }
        }
        isInitialized = true;
    }

    public void updateBoard(Board board)
    {
        board.setFields(fields);
        board.setAllowedToDraw(true);
        board.revalidate();
        board.repaint();
    }

    public void initBoard(Board board)
    {
        removeFields(board);
    }

    public void placeField(JRadioButton rdStart, JRadioButton rdDestination,
                           JRadioButton rdObstacle, int x, int y)
    {
        for (Field[] value : fields) {
            for (int j = 0; j < fields[0].length; j++) {
                if (isCursorOnField(value[j], x, y)) {
                    Field field = value[j];

                    if (rdStart.isSelected()) {
                        if (!field.isStartingPoint() && !existsStartField
                                && !field.isDestination() && !field.isObstacle()) {
                            field.setStartingPoint(true);
                            existsStartField = true;
                            startField = field;
                        } else {
                            if (field.isStartingPoint()) {
                                field.setStartingPoint(false);
                                existsStartField = false;
                                startField = null;
                            }
                        }
                    } else if (rdDestination.isSelected()) {
                        if (!field.isDestination() && !existsDestinationField
                                && !field.isStartingPoint() && !field.isObstacle()) {
                            field.setDestination(true);
                            existsDestinationField = true;
                            destinationField = field;
                        } else {
                            if (field.isDestination()) {
                                field.setDestination(false);
                                existsDestinationField = false;
                                destinationField = null;
                            }
                        }
                    } else if (rdObstacle.isSelected()) {
                        if (!field.isObstacle() && !field.isDestination()
                                && !field.isStartingPoint()) {
                            field.setObstacle(true);
                        } else {
                            field.setObstacle(false);
                        }
                    }
                }
            }
        }
    }

    private boolean isCursorOnField(Field field, int x, int y)
    {
        if (x >= field.getX() && x <= field.getX() + field.getSize())
        {
            return y >= field.getY() && y <= field.getY() + field.getSize();
        }
        return false;
    }

    public void startPathfinder()
    {
        if (existsStartField && existsDestinationField)
        {
            pathfinder = new Pathfinder(startField, destinationField, fields, this);
            setHasStarted(true);
            threadPathfinder = new Thread(() -> {
                pathfinder.start();
                setHasStarted(false);
            });
            threadPathfinder.start();
        }
    }

    public void interruptThread()
    {
        threadPathfinder.interrupt();
    }

    public void removeFields(Board board)
    {
        for (Field[] field : fields) {
            for (int j = 0; j < fields[0].length; j++) {
                field[j].setClosedList(false);
                field[j].setObstacle(false);
                field[j].setOpenList(false);
                field[j].setStartingPoint(false);
                field[j].setPath(false);
                field[j].setDestination(false);
            }
        }

        if (isAnimation() || isSmallSteps())
            interruptThread();

        updateBoard(board);

        existsStartField = false;
        existsDestinationField = false;
        startField = null;
        destinationField = null;

        setAreFieldsDeleted(false);
        setSmallSteps(false);
        setHasSmallStepsStarted(false);
        setAnimationDone(false);
        setHasStarted(false);
        setHasAnimationStarted(false);
    }

    public void generateObstacles(int probability,
                                  Board board)
    {
        removeFields(board);
        Random random = new Random();
        int num;

        for (Field[] field : fields) {
            for (int j = 0; j < fields[0].length; j++) {
                num = random.nextInt(100);
                if (num < probability) {
                    field[j].setObstacle(true);
                }
            }
        }
        updateBoard(board);
    }

    public boolean boardWithoutPathfinder()
    {
        for (Field[] field : fields) {
            for (int j = 0; j < fields[0].length; j++) {
                if (field[j].istClosedList()
                        || field[j].istOpenList() || field[j].isPath()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isReady()
    {
        return startField != null && destinationField != null;
    }

    public boolean hasAnimationStarted()
    {
        return hasAnimationStarted;
    }

    public void setHasAnimationStarted(boolean hasAnimationStarted)
    {
        this.hasAnimationStarted = hasAnimationStarted;
    }

    public boolean isAnimationDone()
    {
        return isAnimationDone;
    }

    public void setAnimationDone(boolean animationDone)
    {
        this.isAnimationDone = animationDone;
    }

    public boolean hasStarted()
    {
        return hasStarted;
    }

    public void setHasStarted(boolean hasStarted)
    {
        this.hasStarted = hasStarted;
    }

    public boolean areFieldsDeleted()
    {
        return areFieldsDeleted;
    }

    public void setAreFieldsDeleted(boolean areFieldsDeleted)
    {
        this.areFieldsDeleted = areFieldsDeleted;
    }

    public int getTimeAnimation()
    {
        return timeAnimation;
    }

    public void setTimeAnimation(int timeAnimation)
    {
        this.timeAnimation = timeAnimation;
    }

    public boolean getIstInitialized()
    {
        return isInitialized;
    }

    public boolean isAnimation()
    {
        return isAnimation;
    }

    public void setAnimation(boolean animation)
    {
        this.isAnimation = animation;
    }

    public boolean isSmallSteps()
    {
        return isSmallSteps;
    }

    public void setSmallSteps(boolean smallSteps)
    {
        this.isSmallSteps = smallSteps;
    }

    public boolean hasSmallStepsStarted()
    {
        return hasSmallStepsStarted;
    }

    public void setHasSmallStepsStarted(boolean hasSmallStepsStarted)
    {
        this.hasSmallStepsStarted = hasSmallStepsStarted;
    }
}
