import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

public class GUI extends JFrame implements MouseListener
{
    public final static int LENGTH = 1000;

    private JRadioButton rdStart;
    private JRadioButton rdDestination;
    private JRadioButton rdObstacle;

    @SuppressWarnings("rawtypes")
    private JComboBox cbBoardSize;
    @SuppressWarnings("rawtypes")
    private JComboBox cbTime;
    @SuppressWarnings("rawtypes")
    private JComboBox cbProbability;

    public static Board board;

    private Controller controller;

    public static void main(String[] args)
    {
        EventQueue.invokeLater(() -> {
            try
            {
                GUI frame = new GUI();
                frame.setVisible(true);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public GUI()
    {
        controller = new Controller();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(LENGTH + 450, LENGTH + 39);
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("A* algorithm");

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        board = new Board();
        board.setBackground(Color.white);
        board.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
                TitledBorder.TOP, null, null));
        board.setBounds(5, 5, 1002, 1002);
        board.addMouseListener(this);
        contentPane.add(board);

        JLabel labelTitel = new JLabel("Pathfinder menu");
        labelTitel.setFont(new Font("Tahoma", Font.PLAIN, 22));
        labelTitel.setBounds(1153, 32, 305, 55);

        contentPane.add(labelTitel);

        JPanel panelBoardSettings = new JPanel();
        panelBoardSettings.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Board settings",
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelBoardSettings.setBounds(1046, 120, 357, 146);
        contentPane.add(panelBoardSettings);
        panelBoardSettings.setLayout(null);

        //noinspection rawtypes
        cbBoardSize = new JComboBox();
        cbBoardSize.setBounds(160, 28, 187, 23);
        panelBoardSettings.add(cbBoardSize);
        //noinspection unchecked,rawtypes
        cbBoardSize.setModel(new DefaultComboBoxModel(new String[] {"10",
                "20", "50", "100", "250", "500"}));

        JLabel lblNewLabel = new JLabel("Board size:");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblNewLabel.setBounds(10, 31, 191, 14);
        panelBoardSettings.add(lblNewLabel);

        JButton btnBretInit = new JButton("Initialize board");
        btnBretInit.addActionListener(arg0 -> clickInitBoard());
        btnBretInit.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnBretInit.setBounds(10, 112, 337, 23);
        panelBoardSettings.add(btnBretInit);

        JPanel panelFieldSettings = new JPanel();
        panelFieldSettings.setBorder(new TitledBorder(null,
                "Field settings", TitledBorder.LEADING, TitledBorder.TOP,
                null, null));
        panelFieldSettings.setLayout(null);
        panelFieldSettings.setBounds(1046, 292, 357, 146);
        contentPane.add(panelFieldSettings);

        JLabel lblFieldsToPlace = new JLabel("Fields to place:");
        lblFieldsToPlace.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblFieldsToPlace.setBounds(6, 29, 210, 14);
        panelFieldSettings.add(lblFieldsToPlace);

        ButtonGroup group = new ButtonGroup();

        rdStart = new JRadioButton("Start");
        rdStart.setFont(new Font("Tahoma", Font.PLAIN, 14));
        rdStart.setBounds(6, 60, 109, 23);
        panelFieldSettings.add(rdStart);

        rdDestination = new JRadioButton("Destination");
        rdDestination.setFont(new Font("Tahoma", Font.PLAIN, 14));
        rdDestination.setBounds(6, 86, 109, 23);
        panelFieldSettings.add(rdDestination);

        rdObstacle = new JRadioButton("Obstacle");
        rdObstacle.setFont(new Font("Tahoma", Font.PLAIN, 14));
        rdObstacle.setBounds(6, 112, 109, 23);
        panelFieldSettings.add(rdObstacle);

        group.add(rdStart);
        group.add(rdDestination);
        group.add(rdObstacle);

        JPanel panelSearchSettings = new JPanel();
        panelSearchSettings.setBorder(new TitledBorder(UIManager
                .getBorder("TitledBorder.border"), "Search settings",
                TitledBorder.LEADING, TitledBorder.TOP, null, null));
        panelSearchSettings.setLayout(null);
        panelSearchSettings.setBounds(1046, 643, 357, 146);
        contentPane.add(panelSearchSettings);

        JLabel lblTimePerStep = new JLabel("Time per step:");
        lblTimePerStep.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblTimePerStep.setBounds(10, 32, 210, 20);
        panelSearchSettings.add(lblTimePerStep);

        JButton btnStartAnimation = new JButton("Start: Animation");
        btnStartAnimation.addActionListener(arg0 -> clickStartSearchAnimation());
        btnStartAnimation.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnStartAnimation.setBounds(10, 73, 337, 23);
        panelSearchSettings.add(btnStartAnimation);

        JButton btnStartSmallSteps = new JButton("Start: In steps");
        btnStartSmallSteps.addActionListener(arg0 -> clickSmallSteps());
        btnStartSmallSteps.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnStartSmallSteps.setBounds(10, 112, 337, 23);
        panelSearchSettings.add(btnStartSmallSteps);

        //noinspection rawtypes
        cbTime = new JComboBox();
        //noinspection unchecked,rawtypes
        cbTime.setModel(new DefaultComboBoxModel(new String[] {"0",
                "250", "500", "1000"}));
        cbTime.setBounds(172, 33, 175, 23);
        panelSearchSettings.add(cbTime);

        JPanel panelObstacleSettings = new JPanel();
        panelObstacleSettings.setBorder(new TitledBorder(null,
                "Obstacle settings", TitledBorder.LEADING,
                TitledBorder.TOP, null, null));
        panelObstacleSettings.setLayout(null);
        panelObstacleSettings.setBounds(1046, 466, 357, 146);
        contentPane.add(panelObstacleSettings);

        JLabel lblProbability = new JLabel("Probability:");
        lblProbability.setFont(new Font("Tahoma", Font.PLAIN, 18));
        lblProbability.setBounds(10, 33, 210, 20);
        panelObstacleSettings.add(lblProbability);

        JButton btnRandomObstacles = new JButton("Random obstacles");
        btnRandomObstacles.addActionListener(arg0 -> clickGenerateObstacles());
        btnRandomObstacles.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnRandomObstacles.setBounds(10, 112, 337, 23);
        panelObstacleSettings.add(btnRandomObstacles);

        //noinspection rawtypes
        cbProbability = new JComboBox();
        //noinspection unchecked,rawtypes
        cbProbability.setModel(new DefaultComboBoxModel(new String[] {"10",
                "20", "30", "40", "50"}));
        cbProbability.setBounds(172, 33, 175, 23);
        panelObstacleSettings.add(cbProbability);

        JButton btnRemoveFields = new JButton("Remove fields");
        btnRemoveFields.addActionListener(arg0 -> clickRemoveFields());
        btnRemoveFields.setFont(new Font("Tahoma", Font.PLAIN, 18));
        btnRemoveFields.setBounds(1046, 979, 357, 23);
        contentPane.add(btnRemoveFields);

        JLabel lblNewLabel_1 = new JLabel("Start");
        lblNewLabel_1.setForeground(Color.GREEN);
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblNewLabel_1.setBounds(1046, 831, 147, 23);
        contentPane.add(lblNewLabel_1);

        JLabel lblZielpunkt = new JLabel("Destination");
        lblZielpunkt.setForeground(Color.RED);
        lblZielpunkt.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblZielpunkt.setBounds(1046, 865, 147, 23);
        contentPane.add(lblZielpunkt);

        JLabel lblHindernis = new JLabel("Obstacle");
        lblHindernis.setForeground(Color.BLACK);
        lblHindernis.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblHindernis.setBounds(1046, 899, 147, 23);
        contentPane.add(lblHindernis);

        JLabel lblWeg = new JLabel("Shortest Path");
        lblWeg.setForeground(Color.BLUE);
        lblWeg.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblWeg.setBounds(1141, 831, 147, 23);
        contentPane.add(lblWeg);

        JLabel lblOpenList = new JLabel("Open list");
        lblOpenList.setForeground(new Color(255, 215, 0));
        lblOpenList.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblOpenList.setBounds(1141, 865, 147, 23);
        contentPane.add(lblOpenList);

        JLabel lblClosedList = new JLabel("Closed list");
        lblClosedList.setForeground(Color.GRAY);
        lblClosedList.setFont(new Font("Tahoma", Font.PLAIN, 16));
        lblClosedList.setBounds(1141, 899, 147, 23);
        contentPane.add(lblClosedList);

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, "Field", TitledBorder.LEADING,
                TitledBorder.TOP, null, null));
        panel.setBounds(1262, 816, 118, 118);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel_2 = new JLabel("F_Cost");
        lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblNewLabel_2.setBounds(10, 26, 46, 14);
        panel.add(lblNewLabel_2);

        JLabel lblGcost = new JLabel("G_Cost");
        lblGcost.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblGcost.setBounds(10, 93, 46, 14);
        panel.add(lblGcost);

        JLabel lblHcost = new JLabel("H_Cost");
        lblHcost.setFont(new Font("Tahoma", Font.PLAIN, 14));
        lblHcost.setBounds(66, 93, 46, 14);
        panel.add(lblHcost);
    }

    private void clickInitBoard()
    {
        int numOfFields;

        numOfFields = Integer
                .parseInt((String) Objects.requireNonNull(cbBoardSize.getSelectedItem()));
        controller.initFields(numOfFields);
        controller.initBoard(board);
    }

    private void clickPlaceField(int x, int y)
    {
        if (controller.getIstInitialized())
        {
            controller.placeField(rdStart, rdDestination, rdObstacle, x, y);
            controller.updateBoard(board);
        }
    }

    private void clickStartSearchAnimation()
    {
        if (controller.getIstInitialized()
                && !controller.isSmallSteps()
                && controller.isReady()
                && !controller.hasAnimationStarted())
        {
            int time;
            time = Integer.parseInt((String) Objects.requireNonNull(cbTime.getSelectedItem()));

            controller.setAnimation(true);
            controller.setSmallSteps(false);
            controller.setHasAnimationStarted(true);
            controller.setTimeAnimation(time);
            controller.startPathfinder();
        }
    }

    private void clickSmallSteps()
    {
        if (controller.getIstInitialized() && !controller.isAnimation()
                && controller.isReady()
                && !controller.isAnimationDone())
        {
            if (!controller.hasSmallStepsStarted())
            {
                controller.setAnimation(false);
                controller.setSmallSteps(true);
                controller.setHasSmallStepsStarted(true);
                controller.startPathfinder();
            }
            else
            {
                if (controller.hasStarted())
                {
                    controller.interruptThread();
                }
            }
        }
    }

    private void clickRemoveFields()
    {
        if (controller.getIstInitialized())
        {
            controller.setAreFieldsDeleted(true);
            controller.removeFields(board);
        }
    }

    private void clickGenerateObstacles()
    {
        if (controller.getIstInitialized()
                && controller.boardWithoutPathfinder())
        {
            int probability;
            probability = Integer.parseInt((String) Objects.requireNonNull(cbProbability
                    .getSelectedItem()));
            controller.generateObstacles(probability, board);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        int x = e.getX();
        int y = e.getY();

        clickPlaceField(x, y);
    }

    @Override
    public void mouseClicked(MouseEvent arg0)
    {
    }

    @Override
    public void mouseEntered(MouseEvent arg0)
    {
    }

    @Override
    public void mouseExited(MouseEvent arg0)
    {
    }

    @Override
    public void mousePressed(MouseEvent arg0)
    {
    }
}
