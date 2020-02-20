import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Pathfinder
{
    private Field startField;
    private Field destinationField;
    private Field[][] fields;
    private Field currentField;

    private ArrayList<Field> openList;
    private ArrayList<Field> closedList;

    private Controller controller;

    private boolean pathFound;

    public Pathfinder(Field startField, Field destinationField, Field[][] fields,
                      Controller controller)
    {
        this.startField = startField;
        this.destinationField = destinationField;
        this.fields = fields;
        this.controller = controller;

        openList = new ArrayList<>();
        closedList = new ArrayList<>();

        openList.add(startField);
    }

    public void start()
    {
        findPath();
    }

    private void findPath()
    {
        while (!pathFound)
        {
            try
            {
                currentField = getLowestFCost(openList);
                removeElementFromList(currentField, openList);
                closedList.add(currentField);
            }
            catch (Exception e)
            {
                System.out.println("No path was found!");
                break;
            }

            if (currentField != startField && currentField != destinationField)
            {
                currentField.setOpenList(false);
                currentField.setClosedList(true);
            }

            if (currentField == destinationField)
            {
                pathFound = true;
                break;
            }

            ArrayList<Field> neighbors = currentField.getNeighbors();

            for (Field neighbor : neighbors) {
                if (hasValidPosition(neighbor)) {
                    neighbor = getNeighborFromList(neighbor.getX(),
                            neighbor.getY(), fields);

                    if (!neighbor.isObstacle()
                            && !isFieldInList(neighbor, closedList)) {
                        if (isFieldInList(neighbor, openList)) {
                            int newGCost;
                            if (isDiagonal(currentField, neighbor)) {
                                newGCost = 14 + currentField.getGCost();
                            } else {
                                newGCost = 10 + currentField.getGCost();
                            }

                            if (newGCost <= neighbor.getGCost()) {
                                neighbor.setGCost(newGCost);
                                neighbor.setFCost(neighbor.getGCost()
                                        + neighbor.getHCost());

                                neighbor.setParent(currentField);
                            }
                        } else {
                            if (isDiagonal(currentField, neighbor)) {
                                neighbor.setGCost(14 + currentField.getGCost());
                            } else {
                                neighbor.setGCost(10 + currentField.getGCost());
                            }
                            neighbor.setHCost(calculateHCost(neighbor, destinationField));
                            neighbor.setFCost(neighbor.getGCost()
                                    + neighbor.getHCost());

                            neighbor.setParent(currentField);

                            openList.add(neighbor);
                            neighbor.setOpenList(true);
                        }
                    }
                }
            }

            if (controller.isAnimation())
            {
                try
                {
                    controller.updateBoard(GUI.board);
                    TimeUnit.MILLISECONDS
                            .sleep(controller.getTimeAnimation());
                }
                catch (InterruptedException e)
                {
                    break;
                }
            }
            else
            {
                try
                {
                    controller.updateBoard(GUI.board);
                    TimeUnit.HOURS.sleep(1);
                }
                catch (InterruptedException e)
                {
                    if (controller.areFieldsDeleted())
                    {
                        controller.setSmallSteps(false);
                        controller.setHasSmallStepsStarted(false);
                        break;
                    }
                }
            }
        }
        if (pathFound)
        {
            Field parent = currentField.getParent();

            while (parent != startField)
            {
                parent.setPath(true);
                parent = parent.getParent();
            }
        }
        controller.updateBoard(GUI.board);

        if (controller.isAnimation())
        {
            controller.setAnimation(false);
            controller.setAnimationDone(true);
        }
    }

    private int calculateHCost(Field field, Field destinationField)
    {
        int distance;
        int xDiff;
        int yDiff;

        xDiff = Math.abs((field.getX() - destinationField.getX())
                / field.getSize());
        yDiff = Math.abs((field.getY() - destinationField.getY())
                / field.getSize());

        distance = (xDiff + yDiff) * 10;
        return distance;
    }

    private boolean isDiagonal(Field fieldOne, Field fieldTwo)
    {
        return fieldOne.getX() != fieldTwo.getX() && fieldOne.getY() != fieldTwo.getY();
    }

    private boolean isFieldInList(Field field, ArrayList<Field> list)
    {
        for (Field value : list) {
            if (field == value) {
                return true;
            }
        }
        return false;
    }

    private Field getNeighborFromList(int x, int y, Field[][] fields)
    {
        for (Field[] field : fields) {
            for (int j = 0; j < fields[0].length; j++) {
                if (field[j].getX() == x && field[j].getY() == y) {
                    return field[j];
                }
            }
        }
        return new Field(-1000, -1000, 0);
    }

    private boolean hasValidPosition(Field field)
    {
        return field.getX() >= 0 && field.getX() <= GUI.LENGTH - field.getSize()
                && field.getY() >= 0
                && field.getY() <= GUI.LENGTH - field.getSize();
    }

    private void removeElementFromList(Field field, ArrayList<Field> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getX() == field.getX()
                    && list.get(i).getY() == field.getY())
            {
                //noinspection SuspiciousListRemoveInLoop
                list.remove(i);
            }
        }
    }

    private Field getLowestFCost(ArrayList<Field> list)
    {
        Field field = list.get(0);
        int fCost = field.getFCost();

        for (Field value : list) {
            if (value.getFCost() <= fCost) {
                field = value;
                fCost = field.getFCost();
            }
        }
        return field;
    }
}
