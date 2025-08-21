package cephra.Frame;

public class Monitor extends javax.swing.JFrame {

    public Monitor() {
        setUndecorated(true);
        initComponents();
        setSize(1000, 750);
        setResizable(false);
        setAppIcon();
        addEscapeKeyListener();
        makeDraggable();
        setLocationByPlatform(false);
        positionTopLeft();
    }

    private void setAppIcon() {
        java.net.URL iconUrl = getClass().getClassLoader().getResource("cephra/Photos/lod.png");
        if (iconUrl != null) {
            setIconImage(new javax.swing.ImageIcon(iconUrl).getImage());
        }
    }

    private void addEscapeKeyListener() {
        addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
                    System.exit(0);
                }
            }
        });
        setFocusable(true);
    }

    private void makeDraggable() {
        final java.awt.Point[] dragPoint = {null};

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                dragPoint[0] = e.getPoint();
            }
        });

        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                if (dragPoint[0] != null) {
                    java.awt.Point currentLocation = getLocation();
                    setLocation(
                        currentLocation.x + e.getX() - dragPoint[0].x,
                        currentLocation.y + e.getY() - dragPoint[0].y
                    );
                }
            }
        });
    }

    private void positionTopLeft() {
        java.awt.Rectangle bounds = java.awt.GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration()
            .getBounds();
        setLocation(bounds.x, bounds.y);
    }

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 750));
        jPanel1.setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/cephra/Photos/QUEUEING - SCREEN SIZE.gif"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 1000, 750);

        getContentPane().setLayout(new java.awt.BorderLayout());
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

        pack();
    }

    public static void main(String args[]) {     
        java.awt.EventQueue.invokeLater(() -> new Monitor().setVisible(true));
    }
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
}
