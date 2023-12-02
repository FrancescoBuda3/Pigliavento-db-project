/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Pigliavento.view;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import Pigliavento.db.Entities.AnnualCollection;
import Pigliavento.db.Entities.Boat;
import Pigliavento.db.Entities.Booking;
import Pigliavento.db.Entities.BookingCount;
import Pigliavento.db.Entities.Customer;
import Pigliavento.db.Entities.Hiker;
import Pigliavento.db.Entities.Instructor;
import Pigliavento.db.Entities.Trip;
import Pigliavento.db.Entities.Umbrella;
import Pigliavento.model.Actions;
import Pigliavento.utils.Pair;

/**
 *
 * @author buda2
 */
public class View extends javax.swing.JFrame {

    private final Actions actions;

    /**
     * Creates new form View
     */
    public View(Actions actions) {
        initComponents();
        this.actions = actions;
        this.setVisible(true);
        update();
    }

    private Date parseDate(String date) {
        if (date.isEmpty()) {
            return null;
        }
        String[] dateSplit = date.split("-");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateSplit[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateSplit[1])-1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateSplit[2]));
        return calendar.getTime();
    }

    private Time parseTime(String time) {
        if (time.isEmpty()) {
            return null;
        }
        String[] timeSplit = time.split(":");
        return Time.valueOf(LocalTime.of(Integer.parseInt(timeSplit[0]), Integer.parseInt(timeSplit[1])));
    }

    private List<Pair<Date, Date>> parsePeriods(String periods) {
        String[] periodsSplit = periods.split(",");
        List<Pair<Date, Date>> periodsList = new ArrayList<>();
        for (String period : periodsSplit) {
            String[] periodSplit = period.split(" ");
            Date start = parseDate(periodSplit[0]);
            Date end = parseDate(periodSplit[1]);
            if (start.compareTo(end) >= 0) {
                JOptionPane.showMessageDialog(this, "La data di inizio del periodo non può essere successiva a quella di fine", "Errore", JOptionPane.ERROR_MESSAGE);
                return null;
            }
            periodsList.add(new Pair<>(start, end));
        }
        return periodsList; 
    }

    private void update() {
        DefaultTableModel model = (DefaultTableModel) tripTable.getModel();
        model.setRowCount(0);
        model = (DefaultTableModel) hikerTable.getModel();
        model.setRowCount(0);
        model = (DefaultTableModel) customerTable.getModel();
        model.setRowCount(0);
        model = (DefaultTableModel) bookingTable.getModel();
        model.setRowCount(0);
        model = (DefaultTableModel) boatTable.getModel();
        model.setRowCount(0);
        model = (DefaultTableModel) instructorTable.getModel();
        model.setRowCount(0);
        loadTrip();
        loadHiker();
        loadCustomer();
        loadBooking();
        loadBoat();
        loadInstructor();
    }

    private void loadTrip() {
        for (Trip t : this.actions.getTrips()) {
            ((javax.swing.table.DefaultTableModel) tripTable.getModel()).addRow(new String[]{
                t.getName(),
                String.valueOf(t.getNumber()),
                t.getDate().toString(),
                t.getStartTime().toString(),
                t.getEndTime().toString(),
                String.valueOf(t.getPrice()),
                t.getBoatModel(),
                String.valueOf(t.getBoatNumber()),
                String.valueOf(t.getnPartecipants()),
                String.valueOf(t.getMaxPartecipants()),
                t.getCfInstructor()
            });
        }
    }

    private void loadHiker() {
        for (Hiker h : this.actions.getHikers()) {
            ((javax.swing.table.DefaultTableModel) hikerTable.getModel()).addRow(new String[]{
                h.getCf(),
                h.getName(),
                h.getSurname(),
                h.getBirthDate().toString(),
                h.getPhone().get().toString(),
                h.getEmail().get().toString(),
                String.valueOf(h.getAccount())
            });
        }
    }

    private void loadCustomer() {
        for (Customer c : this.actions.getCustomers()) {
            ((javax.swing.table.DefaultTableModel) customerTable.getModel()).addRow(new String[]{
                c.getCf(),
                c.getName(),
                c.getSurname(),
                c.getBirthDate().toString(),
                c.getPhone().get().toString(),
                c.getEmail().get().toString()
            });
        }
    }

    private void loadBooking() {
        for (Booking b : this.actions.getBookings()) {
            ((javax.swing.table.DefaultTableModel) bookingTable.getModel()).addRow(new String[]{
                String.valueOf(b.getID()),
                b.getDate().toString(),
                b.getCfCustomer(),
                b.getBathhouse(),
                b.getAddress(),
                String.valueOf(b.getnUmbrella()),
                String.valueOf(b.getAccount()),
                String.valueOf(b.getIsAccountSettled()),
                String.valueOf(b.getDiscount()),
                b.getDiscountDescription(),
            });
        }
    }

    private void loadBoat() {
        for (Boat b : this.actions.getBoats()) {
            ((javax.swing.table.DefaultTableModel) boatTable.getModel()).addRow(new String[]{
                b.getModel(),
                String.valueOf(b.getNumber()),
                String.valueOf(b.getCapacity())
            });
        }
    }

    private void loadInstructor() {
        for (Instructor i : actions.getInstructors()) {
            ((javax.swing.table.DefaultTableModel) instructorTable.getModel()).addRow(new String[]{
                i.getCf(),
                i.getName(),
                i.getSurname(),
                i.getPhone().get().toString(),
                i.getEmail().get().toString()
            });
        }
    }

    private void loadBookingCount() {
        for (BookingCount b : actions.getBookingCountPerBathhouse()) {
            ((javax.swing.table.DefaultTableModel) BookingCountTable.getModel()).addRow(new String[]{
                b.getBathhouse(),
                String.valueOf(b.getCount())
            });
        }
    }

    private void loadAnnualCollection() {
        for (AnnualCollection a : actions.getCollectionPerYear()) {
            ((javax.swing.table.DefaultTableModel) annualCollectionTable.getModel()).addRow(new String[]{
                String.valueOf(a.getYear()),
                String.valueOf(a.getAmount())
            });
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        selectionTab = new javax.swing.JTabbedPane();
        hikerPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tripTable = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        hikerTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        hikerNameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        hikerSurnameField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        hikerCFfield = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        hikerEmailField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        hikerBirthField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        hikerPhoneField = new javax.swing.JTextField();
        hikerRegistrationButton = new javax.swing.JButton();
        update1 = new javax.swing.JButton();
        jLabel48 = new javax.swing.JLabel();
        hikerTripNameField = new javax.swing.JTextField();
        hikerTripNumberField = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        customerPanel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        customerTable = new javax.swing.JTable();
        customerEmailField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        customerBirthField = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        customerPhoneField = new javax.swing.JTextField();
        CustomerRegistrationButton = new javax.swing.JButton();
        update2 = new javax.swing.JButton();
        customerNameField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        customerSurnameField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        customerCFfield = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        CustomerBookingIDfield = new javax.swing.JTextField();
        customerInscriptionCFfield = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        customerInscriptionTripNumberField = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        customerInscriptionTripNameField = new javax.swing.JTextField();
        customerInscriptionButton = new javax.swing.JButton();
        customerCFfield1 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        customerGuestRegistrationButton = new javax.swing.JButton();
        jSeparator3 = new javax.swing.JSeparator();
        bookingPanel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        bookingTable = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        bookingCode = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        bookingOunerCfField = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        bookingBathhouseName = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        bookingBathhouseAddressField = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        bookingUmbrellaNumberField = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        bookingDiscountField = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        bookingDIscountDescriptionField = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        BookingPeriodsField = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        makeBookingButton = new javax.swing.JButton();
        update = new javax.swing.JButton();
        jLabel46 = new javax.swing.JLabel();
        PeriodToSearchUmbrellasField = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        umbrellaTable = new javax.swing.JTable();
        searchButton = new javax.swing.JButton();
        tripPanel = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        boatTable = new javax.swing.JTable();
        jLabel32 = new javax.swing.JLabel();
        tripBoatNameField = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        tripBoatNumberField = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        tripMaxPartField = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        tripDateField = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        tripStartTimeField = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        tripPriceField = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        TripNameField = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        TripNumberField = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        TripInstructorCfField = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        addTripButton = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        instructorTable = new javax.swing.JTable();
        jLabel45 = new javax.swing.JLabel();
        update4 = new javax.swing.JButton();
        statsPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        BookingCountTable = new javax.swing.JTable();
        jScrollPane15 = new javax.swing.JScrollPane();
        annualCollectionTable = new javax.swing.JTable();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        updateStats = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tripTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome", "Numero", "Data", "OraInizio", "OraFine", "Prezzo", "ModelloBarca", "NumeroBarca", "NumPartecipanti", "MaxPartecipanti", "CF_Istruttore"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tripTable);

        jLabel1.setText("ESCURSIONI");

        hikerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codice fiscale", "Nome", "Cognome", "DataNascita", "Telefono", "Mail", "Conto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(hikerTable);

        jLabel2.setText("ESCURSIONISTI");

        hikerNameField.setToolTipText("");
        hikerNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hikerNameFieldActionPerformed(evt);
            }
        });

        jLabel3.setText("Nome");

        hikerSurnameField.setToolTipText("");
        hikerSurnameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hikerSurnameFieldActionPerformed(evt);
            }
        });

        jLabel5.setText("Cognome");

        hikerCFfield.setToolTipText("");
        hikerCFfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hikerCFfieldActionPerformed(evt);
            }
        });

        jLabel6.setText("CodiceFiscale");

        jLabel7.setText("Data di nascita");

        jLabel8.setText("(aaaa-mm-gg)");

        hikerEmailField.setToolTipText("");
        hikerEmailField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hikerEmailFieldActionPerformed(evt);
            }
        });

        jLabel9.setText("Telefono");

        hikerBirthField.setToolTipText("");
        hikerBirthField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hikerBirthFieldActionPerformed(evt);
            }
        });

        jLabel10.setText("Email");

        hikerPhoneField.setToolTipText("");
        hikerPhoneField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hikerPhoneFieldActionPerformed(evt);
            }
        });

        hikerRegistrationButton.setText("REGISTRA UN NUOVO ESCURSIONISTA");
        hikerRegistrationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hikerRegistrationButtonActionPerformed(evt);
            }
        });

        update1.setText("AGGIORNA");
        update1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update1ActionPerformed(evt);
            }
        });

        jLabel48.setText("Numero escursione");

        hikerTripNameField.setToolTipText("");
        hikerTripNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hikerTripNameFieldActionPerformed(evt);
            }
        });

        hikerTripNumberField.setToolTipText("");
        hikerTripNumberField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hikerTripNumberFieldActionPerformed(evt);
            }
        });

        jLabel49.setText("Nome escursione");

        javax.swing.GroupLayout hikerPanelLayout = new javax.swing.GroupLayout(hikerPanel);
        hikerPanel.setLayout(hikerPanelLayout);
        hikerPanelLayout.setHorizontalGroup(
            hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hikerPanelLayout.createSequentialGroup()
                .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(hikerPanelLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel9)
                            .addComponent(jLabel49))
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hikerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(hikerPanelLayout.createSequentialGroup()
                        .addComponent(hikerRegistrationButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(update1))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, hikerPanelLayout.createSequentialGroup()
                        .addComponent(hikerNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hikerSurnameField, javax.swing.GroupLayout.PREFERRED_SIZE, 210, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(hikerCFfield, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, hikerPanelLayout.createSequentialGroup()
                        .addComponent(hikerPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(hikerEmailField))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, hikerPanelLayout.createSequentialGroup()
                        .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(hikerBirthField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, hikerPanelLayout.createSequentialGroup()
                                .addComponent(hikerTripNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addComponent(jLabel48)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(hikerTripNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(hikerPanelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel2))
                    .addGroup(hikerPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 868, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
            .addGroup(hikerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(hikerPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane1)
                        .addContainerGap())
                    .addGroup(hikerPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        hikerPanelLayout.setVerticalGroup(
            hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(hikerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(hikerPanelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hikerNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3)
                            .addComponent(hikerSurnameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(hikerCFfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(hikerBirthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9)
                        .addComponent(jLabel8)
                        .addGap(20, 20, 20)
                        .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(hikerEmailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10)
                            .addComponent(hikerPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel49)
                            .addComponent(hikerTripNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel48)
                            .addComponent(hikerTripNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(hikerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(hikerRegistrationButton)
                            .addComponent(update1)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hikerPanelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 216, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        selectionTab.addTab("Escursionisti", hikerPanel);

        customerTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codice fiscale", "Nome", "Cognome", "Data di nascita", "Telefono", "Email"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(customerTable);

        customerEmailField.setToolTipText("");
        customerEmailField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerEmailFieldActionPerformed(evt);
            }
        });

        jLabel11.setText("Telefono");

        customerBirthField.setToolTipText("");
        customerBirthField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerBirthFieldActionPerformed(evt);
            }
        });

        jLabel12.setText("Email");

        customerPhoneField.setToolTipText("");
        customerPhoneField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerPhoneFieldActionPerformed(evt);
            }
        });

        CustomerRegistrationButton.setText("REGISTRA UN NUOVO CLIENTE");
        CustomerRegistrationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CustomerRegistrationButtonActionPerformed(evt);
            }
        });

        update2.setText("AGGIORNA");
        update2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update2ActionPerformed(evt);
            }
        });

        customerNameField.setToolTipText("");
        customerNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerNameFieldActionPerformed(evt);
            }
        });

        jLabel4.setText("Nome");

        customerSurnameField.setToolTipText("");
        customerSurnameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerSurnameFieldActionPerformed(evt);
            }
        });

        jLabel13.setText("Cognome");

        customerCFfield.setToolTipText("");
        customerCFfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerCFfieldActionPerformed(evt);
            }
        });

        jLabel14.setText("CodiceFiscale");

        jLabel15.setText("Data di nascita");

        jLabel16.setText("(aaaa-mm-gg)");

        jLabel19.setText("Codice Prenotazione Ospitante");

        customerInscriptionCFfield.setToolTipText("");
        customerInscriptionCFfield.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerInscriptionCFfieldActionPerformed(evt);
            }
        });

        jLabel20.setText("CodiceFiscale");

        jLabel21.setText("Nome Escursione");

        customerInscriptionTripNumberField.setToolTipText("");
        customerInscriptionTripNumberField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerInscriptionTripNumberFieldActionPerformed(evt);
            }
        });

        jLabel22.setText("Numero Escursione");

        customerInscriptionTripNameField.setToolTipText("");
        customerInscriptionTripNameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerInscriptionTripNameFieldActionPerformed(evt);
            }
        });

        customerInscriptionButton.setText("ISCRIVI CLIENTE ALL'ESCURSIONE");
        customerInscriptionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerInscriptionButtonActionPerformed(evt);
            }
        });

        customerCFfield1.setToolTipText("");
        customerCFfield1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerCFfield1ActionPerformed(evt);
            }
        });

        jLabel47.setText("CodiceFiscale");

        customerGuestRegistrationButton.setText("REGISTRA UN NUOVO OSPITE");
        customerGuestRegistrationButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerGuestRegistrationButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout customerPanelLayout = new javax.swing.GroupLayout(customerPanel);
        customerPanel.setLayout(customerPanelLayout);
        customerPanelLayout.setHorizontalGroup(
            customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerPanelLayout.createSequentialGroup()
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(customerPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, customerPanelLayout.createSequentialGroup()
                                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(customerInscriptionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(customerPanelLayout.createSequentialGroup()
                                            .addComponent(customerInscriptionTripNameField)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabel22)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(customerInscriptionTripNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(customerInscriptionCFfield, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(customerPanelLayout.createSequentialGroup()
                                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel19)
                                    .addComponent(jLabel47))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(customerCFfield1)
                                    .addGroup(customerPanelLayout.createSequentialGroup()
                                        .addComponent(CustomerBookingIDfield, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE)))))
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, customerPanelLayout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel14)
                            .addComponent(jLabel15)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, customerPanelLayout.createSequentialGroup()
                                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(customerBirthField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel16))
                                .addGap(292, 292, 292))
                            .addGroup(customerPanelLayout.createSequentialGroup()
                                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, customerPanelLayout.createSequentialGroup()
                                        .addComponent(customerPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(20, 20, 20)
                                        .addComponent(jLabel12)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(customerEmailField))
                                    .addComponent(customerCFfield, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, customerPanelLayout.createSequentialGroup()
                                        .addComponent(customerNameField, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                                        .addComponent(jLabel13)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(customerSurnameField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, customerPanelLayout.createSequentialGroup()
                                        .addGap(53, 53, 53)
                                        .addComponent(CustomerRegistrationButton)
                                        .addGap(0, 0, Short.MAX_VALUE)))
                                .addGap(38, 38, 38))))
                    .addGroup(customerPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(customerGuestRegistrationButton)
                        .addGap(181, 181, 181))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, customerPanelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 512, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 833, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(update2))
                .addContainerGap())
        );
        customerPanelLayout.setVerticalGroup(
            customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(customerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 409, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(update2)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(customerPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(customerPanelLayout.createSequentialGroup()
                        .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(customerNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(customerSurnameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(customerCFfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel14))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(customerBirthField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addGap(9, 9, 9)
                .addComponent(jLabel16)
                .addGap(20, 20, 20)
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerEmailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(customerPhoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(CustomerRegistrationButton)
                .addGap(5, 5, 5)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(customerCFfield1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(CustomerBookingIDfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(customerGuestRegistrationButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerInscriptionCFfield, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(customerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(customerInscriptionTripNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(customerInscriptionTripNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22))
                .addGap(18, 18, 18)
                .addComponent(customerInscriptionButton)
                .addGap(18, 18, 18))
        );

        selectionTab.addTab("Clienti", customerPanel);

        bookingTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codice", "Data", "CF del titolare", "Nome Stabilimento", "Indirizzo Stabilimento", "Numero ombrellone", "Conto", "Conto Saldato?", "Sconto Applicato", "Descrizione Sconto"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(bookingTable);

        jLabel23.setText("Codice");

        jLabel24.setText("CF titolare");

        jLabel25.setText("Stabilimento");

        jLabel26.setText("Indirizzo");

        jLabel27.setText("Numero ombrellone");

        jLabel28.setText("Sconto");

        jLabel29.setText("Descrizione Sconto");

        jLabel30.setText("Periodi");

        jLabel31.setText("aaaa-mm-gg aaaa-mm-gg,");

        makeBookingButton.setText("EFFETTUA PRENOTAZIONE");
        makeBookingButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeBookingButtonActionPerformed(evt);
            }
        });

        update.setText("AGGIORNA");
        update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateActionPerformed(evt);
            }
        });

        jLabel46.setText("controlla ombrelloni disponibili nello stabilimento selezionato per il periodo: ");

        PeriodToSearchUmbrellasField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PeriodToSearchUmbrellasFieldActionPerformed(evt);
            }
        });

        umbrellaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nome stabilimento", "Indirizzo stabilimento", "Numero", "Prezzo giornaliero"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane8.setViewportView(umbrellaTable);

        searchButton.setText("CERCA");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout bookingPanelLayout = new javax.swing.GroupLayout(bookingPanel);
        bookingPanel.setLayout(bookingPanelLayout);
        bookingPanelLayout.setHorizontalGroup(
            bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, bookingPanelLayout.createSequentialGroup()
                .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bookingPanelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(bookingPanelLayout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bookingCode, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bookingPanelLayout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bookingOunerCfField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bookingPanelLayout.createSequentialGroup()
                                .addComponent(jLabel25)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bookingBathhouseName, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bookingPanelLayout.createSequentialGroup()
                                .addComponent(jLabel26)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bookingBathhouseAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bookingPanelLayout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bookingUmbrellaNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bookingPanelLayout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bookingDiscountField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(bookingPanelLayout.createSequentialGroup()
                                .addComponent(jLabel29)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(bookingDIscountDescriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(bookingPanelLayout.createSequentialGroup()
                        .addGap(108, 108, 108)
                        .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(makeBookingButton)
                            .addGroup(bookingPanelLayout.createSequentialGroup()
                                .addComponent(jLabel30)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel31)
                                    .addComponent(BookingPeriodsField, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(update))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 53, Short.MAX_VALUE)
                .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(bookingPanelLayout.createSequentialGroup()
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PeriodToSearchUmbrellasField, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(searchButton))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1050, Short.MAX_VALUE)
                    .addComponent(jScrollPane8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        bookingPanelLayout.setVerticalGroup(
            bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bookingPanelLayout.createSequentialGroup()
                .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bookingPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(bookingPanelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel23)
                            .addComponent(bookingCode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel24)
                            .addComponent(bookingOunerCfField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel25)
                            .addComponent(bookingBathhouseName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel26)
                            .addComponent(bookingBathhouseAddressField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(bookingUmbrellaNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel28)
                            .addComponent(bookingDiscountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel29)
                            .addComponent(bookingDIscountDescriptionField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(BookingPeriodsField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46)
                    .addComponent(PeriodToSearchUmbrellasField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(searchButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bookingPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(bookingPanelLayout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addGap(18, 18, 18)
                        .addComponent(makeBookingButton)
                        .addGap(18, 18, 18)
                        .addComponent(update))
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        selectionTab.addTab("Prenotazioni", bookingPanel);

        boatTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Modello", "Numero", "Capienza"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane6.setViewportView(boatTable);

        jLabel32.setText("BARCHE");

        jLabel33.setText("Modello barca");

        jLabel34.setText("Numero barca");

        jLabel35.setText("MaxPartecipanti");

        jLabel36.setText("data");

        jLabel37.setText("Ora inizio");

        jLabel38.setText("aaaa-mm-gg");

        jLabel39.setText("hh:mm:ss");

        jLabel40.setText("Ora fine");

        jLabel41.setText("Prezzo a persona");

        jLabel42.setText("Nome escursione");

        jLabel43.setText("Numero escursione");

        jLabel44.setText("CF Istruttore");

        addTripButton.setText("INSERISCI NUOVA ESCURSIONE");
        addTripButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addTripButtonActionPerformed(evt);
            }
        });

        instructorTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Codice fiscale", "Nome", "Cognome", "Telefono", "Email"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane7.setViewportView(instructorTable);

        jLabel45.setText("ISTRUTTORI");

        update4.setText("AGGIORNA");
        update4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                update4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tripPanelLayout = new javax.swing.GroupLayout(tripPanel);
        tripPanel.setLayout(tripPanelLayout);
        tripPanelLayout.setHorizontalGroup(
            tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tripPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tripPanelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tripBoatNameField))
                    .addGroup(tripPanelLayout.createSequentialGroup()
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(jLabel36)
                            .addComponent(jLabel37)
                            .addComponent(jLabel40))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tripDateField)
                            .addComponent(tripStartTimeField)
                            .addComponent(jTextField6)
                            .addGroup(tripPanelLayout.createSequentialGroup()
                                .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel38)
                                    .addComponent(jLabel39)
                                    .addComponent(tripBoatNumberField, javax.swing.GroupLayout.DEFAULT_SIZE, 93, Short.MAX_VALUE)
                                    .addComponent(tripMaxPartField))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(tripPanelLayout.createSequentialGroup()
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel42)
                            .addComponent(jLabel41)
                            .addComponent(jLabel43))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TripNameField)
                            .addGroup(tripPanelLayout.createSequentialGroup()
                                .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(TripNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tripPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(tripPanelLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(TripInstructorCfField)
                            .addGroup(tripPanelLayout.createSequentialGroup()
                                .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(addTripButton)
                                    .addComponent(update4))
                                .addGap(0, 136, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32))
                .addGap(12, 12, 12)
                .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel45)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        tripPanelLayout.setVerticalGroup(
            tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tripPanelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(jLabel45))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tripPanelLayout.createSequentialGroup()
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel33)
                            .addComponent(tripBoatNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tripBoatNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tripMaxPartField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tripDateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36))
                        .addGap(4, 4, 4)
                        .addComponent(jLabel38)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tripStartTimeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel37))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel40))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel39)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(tripPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel41))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TripNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TripNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel43))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(tripPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(TripInstructorCfField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addTripButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(update4)
                        .addGap(15, 15, 15))
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        selectionTab.addTab("Escursioni", tripPanel);

        BookingCountTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Stabilimento", "Indirizzo", "Numero Prenotazioni"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(BookingCountTable);

        annualCollectionTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Anno", "Incasso Annuo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane15.setViewportView(annualCollectionTable);

        jLabel74.setText("TOTALE PRENOTAZIONI PER STABILIMENTO");

        jLabel75.setText("INCASSO PER OGNI ANNO");

        updateStats.setText("AGGIORNA");
        updateStats.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateStatsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout statsPanelLayout = new javax.swing.GroupLayout(statsPanel);
        statsPanel.setLayout(statsPanelLayout);
        statsPanelLayout.setHorizontalGroup(
            statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statsPanelLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel74))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel75)
                    .addGroup(statsPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane15, javax.swing.GroupLayout.PREFERRED_SIZE, 485, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(updateStats)))
                .addContainerGap(333, Short.MAX_VALUE))
        );
        statsPanelLayout.setVerticalGroup(
            statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, statsPanelLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(jLabel75))
                .addGroup(statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(statsPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(statsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane15, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addGroup(statsPanelLayout.createSequentialGroup()
                        .addGap(183, 183, 183)
                        .addComponent(updateStats)))
                .addContainerGap())
        );

        selectionTab.addTab("Statistiche", statsPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(selectionTab, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(selectionTab, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void hikerSurnameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hikerSurnameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hikerSurnameFieldActionPerformed

    private void hikerCFfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hikerCFfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hikerCFfieldActionPerformed

    private void hikerEmailFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hikerEmailFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hikerEmailFieldActionPerformed

    private void hikerBirthFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hikerBirthFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hikerBirthFieldActionPerformed

    private void hikerPhoneFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hikerPhoneFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hikerPhoneFieldActionPerformed

    private void hikerRegistrationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hikerRegistrationButtonActionPerformed
        String cf = this.hikerCFfield.getText();
        String name = this.hikerNameField.getText();
        String surname = this.hikerSurnameField.getText();
        Date birth = this.parseDate(this.hikerBirthField.getText());
        Optional<String> phone = this.hikerPhoneField.getText() == "" ? Optional.empty() : Optional.of(this.hikerPhoneField.getText());
        Optional<String> email = this.hikerEmailField.getText() == "" ? Optional.empty() : Optional.of(this.hikerEmailField.getText());
        float account = 0;
        String tripName = this.hikerTripNameField.getText();
        String tripNumberString = this.hikerTripNumberField.getText();
        if (cf.isEmpty() || name.isEmpty() || surname.isEmpty() || birth == null || tripName.isEmpty() ) {
            JOptionPane.showMessageDialog(this, "Inserire tutti i campi obbligatori");
            return;
        }
        int tripNumber = Integer.parseInt(tripNumberString);
        if(!this.actions.addHiker(new Hiker(cf, name, surname, birth, phone, email, account))){
            JOptionPane.showMessageDialog(this, "Errore nell'inserimento");
            return;
        };
        if (!this.actions.RegisterHiker(cf, tripName, tripNumber)) {
            JOptionPane.showMessageDialog(this, "Errore nell'inserimento");
            return;
        };
        this.hikerCFfield.setText("");
        this.hikerNameField.setText("");
        this.hikerSurnameField.setText("");
        this.hikerBirthField.setText("");
        this.hikerPhoneField.setText("");
        this.hikerEmailField.setText("");
        this.hikerTripNameField.setText("");
        this.hikerTripNumberField.setText("");
        this.update();
    }//GEN-LAST:event_hikerRegistrationButtonActionPerformed

    private void customerEmailFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CustomerEmailFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CustomerEmailFieldActionPerformed

    private void customerBirthFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerBirthFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerBirthFieldActionPerformed

    private void customerPhoneFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerPhoneFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerPhoneFieldActionPerformed

    private void CustomerRegistrationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CustomerRegistrationButtonActionPerformed
        String name = this.customerNameField.getText();
        String surname = this.customerSurnameField.getText();
        String cf = this.customerCFfield.getText();
        Date birth = parseDate(this.customerBirthField.getText());
        Optional<String> phone = this.customerPhoneField.getText() == "" ? Optional.empty() : Optional.of(this.customerPhoneField.getText());
        Optional<String> email = this.customerEmailField.getText() == "" ? Optional.empty() : Optional.of(this.customerEmailField.getText());

        if (name.isEmpty() || surname.isEmpty() || cf.isEmpty() || birth == null) {
            JOptionPane.showMessageDialog(this, "Inserire tutti i campi obbligatori");
            return;
        }

        this.customerNameField.setText("");
        this.customerSurnameField.setText("");
        this.customerCFfield.setText("");
        this.customerBirthField.setText("");
        this.customerPhoneField.setText("");
        this.customerEmailField.setText("");

        if (!this.actions.addCustomer(new Customer(cf, name, surname, birth, phone, email))){
            JOptionPane.showMessageDialog(this, "Errore nell'inserimento");
            return;
        };

        this.update();        
    }//GEN-LAST:event_CustomerRegistrationButtonActionPerformed

    private void customerNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerNameFieldActionPerformed

    private void customerSurnameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerSurnameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerSurnameFieldActionPerformed

    private void customerCFfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerCFfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerCFfieldActionPerformed

    private void customerInscriptionCFfieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerInscriptionCFfieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerInscriptionCFfieldActionPerformed

    private void customerInscriptionTripNumberFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerInscriptionTripNumberFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerInscriptionTripNumberFieldActionPerformed

    private void customerInscriptionTripNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerInscriptionTripNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerInscriptionTripNameFieldActionPerformed

    private void customerInscriptionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerInscriptionButtonActionPerformed
        String cf = this.customerInscriptionCFfield.getText();
        String tripName = this.customerInscriptionTripNameField.getText();
        String tripNumberString = this.customerInscriptionTripNumberField.getText();
        if (cf.isEmpty() || tripName.isEmpty() || tripNumberString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserire tutti i campi obbligatori");
            return;
        }
        int tripNumber = Integer.parseInt(tripNumberString);
        if(!this.actions.RegisterCustomer(cf, tripName, tripNumber)) {
            JOptionPane.showMessageDialog(this, "Errore nell'inserimento");
            return;
        }
        this.customerInscriptionCFfield.setText("");
        this.customerInscriptionTripNameField.setText("");
        this.customerInscriptionTripNumberField.setText("");
        this.update();
    }//GEN-LAST:event_customerInscriptionButtonActionPerformed

    private void addTripButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addTripButtonActionPerformed
        String boatModel = this.tripBoatNameField.getText();
        String boatNumberString = this.tripBoatNumberField.getText();
        String maxPartString = this.tripMaxPartField.getText();
        String dateString = this.tripDateField.getText();
        String startTimeString = this.tripStartTimeField.getText();
        String endTimeString = this.jTextField6.getText();
        String priceString = this.tripPriceField.getText();
        String tripName = this.TripNameField.getText();
        String tripNumberString = this.TripNumberField.getText();
        String instructorCf = this.TripInstructorCfField.getText();
        if (boatModel.isEmpty() || boatNumberString.isEmpty() || maxPartString.isEmpty() || dateString.isEmpty() || startTimeString.isEmpty() || endTimeString.isEmpty() || priceString.isEmpty() || tripName.isEmpty() || tripNumberString.isEmpty() || instructorCf.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserire tutti i campi obbligatori");
            return;
        }
        int boatNumber = Integer.parseInt(boatNumberString);
        int maxPartecipants = Integer.parseInt(maxPartString);
        Date date = parseDate(dateString);
        Time startTime = parseTime(startTimeString);
        Time endTime = parseTime(endTimeString);
        float price = Float.parseFloat(priceString);
        int tripNumber = Integer.parseInt(tripNumberString);
        if (!this.actions.addTrip(new Trip(maxPartecipants, tripName, tripNumber, date, startTime, endTime, price, boatModel, boatNumber, instructorCf, 0))){
            JOptionPane.showMessageDialog(this, "Errore nell'inserimento");
            return;
        }
        this.tripBoatNameField.setText("");
        this.tripBoatNumberField.setText("");
        this.tripMaxPartField.setText("");
        this.tripDateField.setText("");
        this.tripStartTimeField.setText("");
        this.jTextField6.setText("");
        this.tripPriceField.setText("");
        this.TripNameField.setText("");
        this.TripNumberField.setText("");
        this.TripInstructorCfField.setText("");
        this.update();
    }//GEN-LAST:event_addTripButtonActionPerformed

    private void update4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update4ActionPerformed
        this.update();
    }//GEN-LAST:event_update4ActionPerformed

    private void updateStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateStatsActionPerformed
        DefaultTableModel model = (DefaultTableModel) BookingCountTable.getModel();
        model.setRowCount(0);
        DefaultTableModel model2 = (DefaultTableModel) annualCollectionTable.getModel();
        model2.setRowCount(0);
        List<AnnualCollection> annualCollections = this.actions.getCollectionPerYear();
        List<BookingCount> bookingCounts = this.actions.getBookingCountPerBathhouse();
        for (AnnualCollection b : annualCollections) {
            ((javax.swing.table.DefaultTableModel) annualCollectionTable.getModel()).addRow(new String[]{
                String.valueOf(b.getYear()),
                String.valueOf(b.getAmount())
            });
        }

        for (BookingCount b : bookingCounts) {
            ((javax.swing.table.DefaultTableModel) BookingCountTable.getModel()).addRow(new String[]{
                b.getBathhouse(),
                b.getAddress(),
                String.valueOf(b.getCount())
            });
        }
    }//GEN-LAST:event_updateStatsActionPerformed

    private void hikerNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hikerNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hikerNameFieldActionPerformed

    private void customerCFfield1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerCFfield1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_customerCFfield1ActionPerformed

    private void customerGuestRegistrationButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerGuestRegistrationButtonActionPerformed
        String cf = this.customerCFfield1.getText();
        String BookingIdString = this.CustomerBookingIDfield.getText();
        if (cf.isEmpty() || BookingIdString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserire tutti i campi obbligatori");
            return;
        }
        int BookingId = Integer.parseInt(BookingIdString);
        if (!this.actions.addGuest(BookingId, cf)){
            JOptionPane.showMessageDialog(this, "Errore nell'inserimento");
            return;
        }
        this.customerCFfield1.setText("");
        this.CustomerBookingIDfield.setText("");
        this.update();
    }//GEN-LAST:event_customerGuestRegistrationButtonActionPerformed

    private void hikerTripNameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hikerTripNameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hikerTripNameFieldActionPerformed

    private void hikerTripNumberFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hikerTripNumberFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hikerTripNumberFieldActionPerformed

    private void update1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update1ActionPerformed
        this.update();
    }//GEN-LAST:event_update1ActionPerformed

    private void update2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_update2ActionPerformed
        update();
    }//GEN-LAST:event_update2ActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        String periodString = this.PeriodToSearchUmbrellasField.getText();
        String bathhouseName = this.bookingBathhouseName.getText();
        String bathhouseAddress = this.bookingBathhouseAddressField.getText();
        if (periodString.isEmpty() || bathhouseName.isEmpty() || bathhouseAddress.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserire tutti i campi obbligatori");
            return;
        }
        List<Pair<Date, Date>> periods = parsePeriods(periodString);
        Pair<Date, Date> period = periods.get(0);
        List<Umbrella> umbrellas = this.actions.getAvailableUmbrellasInBathhouse(bathhouseName, bathhouseAddress, period.getFirst(), period.getSecond());
        DefaultTableModel model = (DefaultTableModel) this.umbrellaTable.getModel();
        model.setRowCount(0);
        for (Umbrella umbrella : umbrellas) {
            model.addRow(new Object[]{umbrella.getBathhouse(), umbrella.getAddress(), umbrella.getNumber(), umbrella.getDailyPrice()});
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void PeriodToSearchUmbrellasFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PeriodToSearchUmbrellasFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PeriodToSearchUmbrellasFieldActionPerformed

    private void updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateActionPerformed
        this.update();
    }//GEN-LAST:event_updateActionPerformed

    private void makeBookingButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeBookingButtonActionPerformed
        String codeString = this.bookingCode.getText();
        String bathhouseName = this.bookingBathhouseName.getText();
        String bathhouseAddress = this.bookingBathhouseAddressField.getText();
        String umbrellaNumberString = this.bookingUmbrellaNumberField.getText();
        String discountString = this.bookingDiscountField.getText();
        String discountDescription = this.bookingDIscountDescriptionField.getText();
        String ownerCf = this.bookingOunerCfField.getText();
        String periodsString = this.BookingPeriodsField.getText();
        if (codeString.isEmpty() || bathhouseName.isEmpty() || bathhouseAddress.isEmpty() || umbrellaNumberString.isEmpty() || discountString.isEmpty() || ownerCf.isEmpty() || periodsString.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Inserire tutti i campi obbligatori");
            return;
        }
        int code = Integer.parseInt(codeString);
        int umbrellaNumber = Integer.parseInt(umbrellaNumberString);
        float discount = Float.parseFloat(discountString);
        List<Pair<Date, Date>> periods = parsePeriods(periodsString);
        if (periods == null) {
            JOptionPane.showMessageDialog(this, "Errore nel formato dei periodi");
            return;
        }
        if (!this.actions.addBooking(ownerCf, umbrellaNumber,  bathhouseName, bathhouseAddress, code, periods)){
            JOptionPane.showMessageDialog(this, "Errore nell'inserimento");
            return;
        }
        if (!actions.addDiscountToBooking(code, discount, discountDescription)) {
            JOptionPane.showMessageDialog(this, "Errore nell'inserimento");
            return;
        }
        this.bookingCode.setText("");
        this.bookingBathhouseName.setText("");
        this.bookingBathhouseAddressField.setText("");
        this.bookingUmbrellaNumberField.setText("");
        this.bookingDiscountField.setText("");
        this.bookingDIscountDescriptionField.setText("");
        this.bookingOunerCfField.setText("");
        this.BookingPeriodsField.setText("");
        this.update();
    }//GEN-LAST:event_makeBookingButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(View.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable BookingCountTable;
    private javax.swing.JTextField BookingPeriodsField;
    private javax.swing.JTextField CustomerBookingIDfield;
    private javax.swing.JButton CustomerRegistrationButton;
    private javax.swing.JTextField PeriodToSearchUmbrellasField;
    private javax.swing.JTextField TripInstructorCfField;
    private javax.swing.JTextField TripNameField;
    private javax.swing.JTextField TripNumberField;
    private javax.swing.JButton addTripButton;
    private javax.swing.JTable annualCollectionTable;
    private javax.swing.JTable boatTable;
    private javax.swing.JTextField bookingBathhouseAddressField;
    private javax.swing.JTextField bookingBathhouseName;
    private javax.swing.JTextField bookingCode;
    private javax.swing.JTextField bookingDIscountDescriptionField;
    private javax.swing.JTextField bookingDiscountField;
    private javax.swing.JTextField bookingOunerCfField;
    private javax.swing.JPanel bookingPanel;
    private javax.swing.JTable bookingTable;
    private javax.swing.JTextField bookingUmbrellaNumberField;
    private javax.swing.JTextField customerBirthField;
    private javax.swing.JTextField customerCFfield;
    private javax.swing.JTextField customerCFfield1;
    private javax.swing.JTextField customerEmailField;
    private javax.swing.JButton customerGuestRegistrationButton;
    private javax.swing.JButton customerInscriptionButton;
    private javax.swing.JTextField customerInscriptionCFfield;
    private javax.swing.JTextField customerInscriptionTripNameField;
    private javax.swing.JTextField customerInscriptionTripNumberField;
    private javax.swing.JTextField customerNameField;
    private javax.swing.JPanel customerPanel;
    private javax.swing.JTextField customerPhoneField;
    private javax.swing.JTextField customerSurnameField;
    private javax.swing.JTable customerTable;
    private javax.swing.JTextField hikerBirthField;
    private javax.swing.JTextField hikerCFfield;
    private javax.swing.JTextField hikerEmailField;
    private javax.swing.JTextField hikerNameField;
    private javax.swing.JPanel hikerPanel;
    private javax.swing.JTextField hikerPhoneField;
    private javax.swing.JButton hikerRegistrationButton;
    private javax.swing.JTextField hikerSurnameField;
    private javax.swing.JTable hikerTable;
    private javax.swing.JTextField hikerTripNameField;
    private javax.swing.JTextField hikerTripNumberField;
    private javax.swing.JTable instructorTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JButton makeBookingButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JTabbedPane selectionTab;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JTextField tripBoatNameField;
    private javax.swing.JTextField tripBoatNumberField;
    private javax.swing.JTextField tripDateField;
    private javax.swing.JTextField tripMaxPartField;
    private javax.swing.JPanel tripPanel;
    private javax.swing.JTextField tripPriceField;
    private javax.swing.JTextField tripStartTimeField;
    private javax.swing.JTable tripTable;
    private javax.swing.JTable umbrellaTable;
    private javax.swing.JButton update;
    private javax.swing.JButton update1;
    private javax.swing.JButton update2;
    private javax.swing.JButton update4;
    private javax.swing.JButton updateStats;
    // End of variables declaration//GEN-END:variables
}
