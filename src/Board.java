import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class Board extends JPanel
{
    private Field[][] fields;
    private boolean isAllowedToDraw = false;

    public void drawFields(Graphics2D g)
    {
        for (Field[] field : fields) {
            for (int j = 0; j < fields[0].length; j++) {
                field[j].drawField(g);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (isAllowedToDraw)
        {
            drawFields(g2);
        }
    }

    public void setAllowedToDraw(boolean allowedToDraw)
    {
        this.isAllowedToDraw = allowedToDraw;
    }

    public void setFields(Field[][] fields)
    {
        this.fields = fields;
    }
}
