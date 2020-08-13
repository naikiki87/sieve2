package dbies;

import dbies.task.Job;
import dbies.task.JobMappingReaderIE;
import dbies.task.JobViewer;
import dbies.task.popup.CopyTaskAndDDL;
import dbies.task.popup.InsertIpTableTask;
import dbies.task.popup.InsertQuery;
import dbies.task.popup.PlanGeneration;
import dbies.task.popup.RemoveTask;
import dbies.task.popup.SelectJobType;
import dbies.task.popup.SelectTaskTemplate;
import dbies.task.popup.TaskInsertion;
import dbies.task.properties.Aggregation;
import dbies.task.properties.Analysis;
import dbies.task.properties.Association;
import dbies.task.properties.Basestream;
import dbies.task.properties.BinaryJoin_mm;
import dbies.task.properties.BinaryJoin_ms;
import dbies.task.properties.Clustering;
import dbies.task.properties.Context;
import dbies.task.properties.ContextRule;
import dbies.task.properties.ContextSequence;
import dbies.task.properties.ExpressionProp;
import dbies.task.properties.Final;
import dbies.task.properties.FrequentItemsets;
import dbies.task.properties.HalfJoinPlus;
import dbies.task.properties.MJoin;
import dbies.task.properties.MergeJob;
import dbies.task.properties.Olap;
import dbies.task.properties.OneItemFrequent;
import dbies.task.properties.Period;
import dbies.task.properties.Projection;
import dbies.task.properties.Selection;
import dbies.task.properties.Selection_ms;
import dbies.task.properties.SequencePatterns;
import dbies.task.properties.SocketOutput;
import dbies.task.properties.Split;
import dbies.task.properties.Task;
import dbies.task.properties.TopK;
import dbies.task.properties.Transaction;
import dbies.task.properties.Union;
import dbies.task.properties.VelocityProp;
import dbies.task.properties.adhoc.KospiIndex;
import dbies.task.properties.mapreduce.RMI_Controller;
import dblab.core.server.deployer.ExecutionTerminationException;
import dblab.core.server.deployer.IEDeployer;
import dblab.core.server.descriptor.IEDescriptor;
import dblab.core.server.descriptor.ddl.Attribute;
import dblab.core.server.descriptor.ddl.DataDefinition;
import dblab.core.server.descriptor.ddl.DataOperation;
import dblab.core.server.descriptor.ddl.FixedPosition;
import dblab.core.server.descriptor.ddl.NodeInfo;
import dblab.core.server.descriptor.ddl.PropertyOperation;
import dblab.core.server.descriptor.ddl.TemplateOperation;
import dblab.core.server.descriptor.ddl.TemplateSource;
import dblab.core.server.iptabledescriptor.ipTableOperator;
import dblab.core.server.iptabledescriptor.ipTableTaskInfo;
import dblab.core.task.TaskState;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Panel;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.accessibility.AccessibleContext;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DefaultXYItemRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class iesMainFrm extends JFrame
{
  static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(iesMainFrm.class);
  public JPanel cur_view;
  public DataDefinition current_ddl = null; public DataDefinition old_ddl = null;
  public DataOperation ol;
  public PropertyOperation tl;
  public IEDeployer doo;
  public ipTableOperator ipTableTasks;
  public ipTableTaskInfo cur_task;
  public double cpuUsage = 0.0D;
  public double sumCpuUsage = 0.0D;
  public int countCpuUsage = 0;
  public double memUsage = 0.0D;
  ArrayList<Double> memInfo = new ArrayList();
  public long currentSystemTime = 0L;
  public String strSystemTime = "";

  String filename = "config/DataDefinition.xml";
  String out_filename = "config/DataDefinition.xml";
  int sizeOfDouble = 64;
  int sizeOfLong = 64;
  int sizeOfInteger = 32;
  int cur_ddl_id = -1;
  String cur_user_name = "";
  public NodeInfo popnode;
  int old_attributes;
  boolean create_mode = false;
  int new_ddl_id;
  long jobId;
  long cur_taskId = -1L;
  DefaultTableModel[] TaskModelList;
  private DefaultMutableTreeNode datanode;
  DefaultMutableTreeNode selectedNode;
  public JobViewer job_viewer;
  Vector<Long> taskId_list;
  TaskStatePlayer player;
  String loginedName = "dblab";
  boolean viewAllUser = false;
  long sessionId;
  private ImageIcon add_ddl = new ImageIcon(getClass().getResource("/dbies/imgs/add_ddl.png"));
  private ImageIcon add_task = new ImageIcon(getClass().getResource("/dbies/imgs/add_task.png"));
  private ImageIcon run = new ImageIcon(getClass().getResource("/dbies/imgs/run.png"));
  private ImageIcon run_d = new ImageIcon(getClass().getResource("/dbies/imgs/run_d.png"));
  private ImageIcon stop = new ImageIcon(getClass().getResource("/dbies/imgs/stop.png"));
  private ImageIcon stop_d = new ImageIcon(getClass().getResource("/dbies/imgs/stop_d.png"));
  private ImageIcon rerun = new ImageIcon(getClass().getResource("/dbies/imgs/rerun.png"));
  private ImageIcon rerun_d = new ImageIcon(getClass().getResource("/dbies/imgs/rerun_d.png"));
  private ImageIcon pause = new ImageIcon(getClass().getResource("/dbies/imgs/pause.png"));
  private ImageIcon pause_d = new ImageIcon(getClass().getResource("/dbies/imgs/pause_d.png"));
  public TemplateOperation templateOper;
  public DataOperation mapReduceDO;
  public PropertyOperation mapReducePO;
  public IEDeployer mapReduceIED;
  public int selectedTaskID = -1;
  Timer systemInfoTimer;
  boolean onSystemInfoTimer = false;
  int baseCount = 0;

  boolean onExportSystemInfoTimer = false;

  Map<Long, Timer> exportSystemInfoTimerList = new HashMap();

  boolean onSystemMonitorDialog = false;

  Map<Long, Boolean> exportTaskIDList = new HashMap();
  private TimeSeries series1;
  private TimeSeries series2;
  private TimeSeries[] seriesArray;
  boolean onViewGraph = false;

  boolean mapReduceRMI_InfoFlag = false;
  private JButton AddField;
  private JButton CommitField;
  private JPanel Controllers;
  private JButton CreateAddField;
  private JButton CreateCancle;
  private JRadioButton CreateFieldTypeFixed;
  private JRadioButton CreateFieldTypeVariable;
  private JButton CreateOK;
  private JLabel CreateOwnerName;
  private JButton CreateRemoveField;
  private JPanel DBMainList;
  private JScrollPane DBMainListScroll;
  private JTable DBMainListTable;
  private JTextArea DDLCreateComment;
  private JDialog DDLCreateDialog;
  private JTextField DDLCreateName;
  private JTable DDLCreateTable;
  private JPanel DDLData;
  private JTable DDLDataTable;
  private JTextArea DDLDefComment;
  private JLabel DDLDefDate;
  private JLabel DDLDefName;
  private JTable DDLDefTable;
  private JPanel DDLDefinition;
  private JPanel DDLDetail;
  private JTabbedPane DDLDetailTab;
  private JPanel DDLList;
  private JScrollPane DDLListScroll;
  private JTable DDLListTable;
  private JTextArea DDLStrComment;
  private JLabel DDLStrFieldDel;
  private JPanel DDLStrFixedPanel;
  private JTable DDLStrFixedTable;
  private JLabel DDLStrName;
  private JLabel DDLStrRecordDel;
  private JPanel DDLStrSetting;
  private JTable DDLStrTable;
  private JLabel DDLStrType;
  private JPanel DDLStrVariablePanel;
  private JPanel DDLStructure;
  private ButtonGroup DelimeterMode;
  private JToolBar DetailToolBar;
  private JToolBar DetailToolBar1;
  private JTextField FieldAddDefault;
  private JDialog FieldAddDialog;
  private JLabel FieldAddFrameName;
  private JTextField FieldAddName;
  private JCheckBox FieldAddNull;
  private JComboBox FieldAddType;
  private JRadioButton FieldTypeFixed;
  private JRadioButton FieldTypeVariable;
  private JDialog FixedDialog;
  private JFrame FixedFrame;
  private JTable FixedTable;
  private JButton IP_TableRefreshButton;
  private JTextField ItemsetDelimeter;
  private JRadioButton ItemsetDifferent;
  private JRadioButton ItemsetSame;
  private ButtonGroup ItemsetSelectGroup;
  private JTable ItemsetTable;
  private JScrollPane ItemsetTableScroll;
  private JSplitPane MainFrame;
  private JButton MapReduceRunBtn;
  private JButton MapReduceStopBtn;
  private JMenuBar MenuBar;
  private JSplitPane NaviFrame;
  private JLabel NaviLabel;
  private JScrollPane NaviScroll;
  private JTree NaviTree;
  private JSplitPane Navis;
  private JLabel OnwerLabel;
  private JDialog PipeSelectDlg;
  private JTable PipeSelectTable;
  private JPopupMenu PopupMenu1;
  private JPopupMenu PopupMenu2;
  private JButton RemoveField;
  private JPanel TaskList;
  private JScrollPane TaskListScroll;
  private JTable TaskListTable;
  private JButton TaskPauseTBtn;
  private JButton TaskRerunTBtn;
  private JButton TaskRunTBtn;
  private JButton TaskStopTBtn;
  private JPanel TaskUserList;
  private JTable TaskUserListTable;
  private JSplitPane TasksFrame;
  private JScrollPane TasksScroll;
  private JTable TasksTable1;
  private JTextField TimerPeriodTextField;
  private JScrollPane TuskUserListScroll;
  private JComboBox TypeComboBox;
  private JCheckBox UseItemsetCheck;
  private JPanel UserList;
  private JScrollPane UserListScroll;
  private JTable UserListTable;
  private JTextField VFFieldDelimeter;
  private JTextField VFRecordDelimeter;
  private JDialog VariableDialog;
  private JComboBox WorkerComboBox;
  private JButton applyButton;
  private JTextField autoTerminateChangeTextField;
  private JLabel autoTerminateOutputCountLabel;
  private JTextField autoTerminateRepeatTextField;
  private JLabel autoTerminateTupleRepeatLabel;
  private JMenuItem copyDDLMenuItem;
  private JLabel cpuUsageLabel;
  private JButton createIndetailButton;
  private JLabel elapsedTimeLabel;
  private JLabel endTimeLabel;
  private JLabel executionStateLabel;
  private JButton exportButton;
  private JTextField exportLocationTextField;
  private JTextField heartbeatPortTextField;
  private JButton indetailButton;
  private JPanel informationPanel;
  private JTextField inputPortTextField;
  private JLabel inputRateLabel;
  private JTextField ipAddressTextField;
  private JPopupMenu ipTablePopUp;
  private JPanel ipTableTaskInfoPanel;
  private JPanel ipTableTaskListPanel;
  private JTable ipTableTaskListTable;
  private JMenuItem item_copyTask;
  private JMenuItem item_display;
  private JMenuItem item_hidden;
  private JMenuItem item_insertJob;
  private JMenuItem item_insertQuery2;
  private JMenuItem item_insertTask;
  private JMenuItem item_insert_IP_Table;
  private JMenuItem item_jobProperty;
  private JMenuItem item_makeTemplate;
  private JMenuItem item_parameter;
  private JMenuItem item_planGeneration;
  private JMenuItem item_preloadTask;
  private JMenuItem item_removeJob;
  private JMenuItem item_removeTask;
  private JMenuItem item_removeTask1;
  private JMenuItem item_runTask;
  private JMenuItem item_runTask1;
  private JMenuItem item_stopTask;
  private JMenuItem item_stopTask1;
  private JMenuItem item_taskProperty;
  private JMenuItem item_taskProperty1;
  private JButton jButton1;
  private JButton jButton10;
  private JButton jButton11;
  private JButton jButton12;
  private JButton jButton13;
  private JButton jButton14;
  private JButton jButton15;
  private JButton jButton2;
  private JButton jButton3;
  private JButton jButton4;
  private JButton jButton5;
  private JButton jButton6;
  private JButton jButton7;
  private JButton jButton8;
  private JButton jButton9;
  private JComboBox jComboBox1;
  private JLabel jLabel1;
  private JLabel jLabel10;
  private JLabel jLabel11;
  private JLabel jLabel12;
  private JLabel jLabel13;
  private JLabel jLabel14;
  private JLabel jLabel15;
  private JLabel jLabel16;
  private JLabel jLabel17;
  private JLabel jLabel18;
  private JLabel jLabel19;
  private JLabel jLabel2;
  private JLabel jLabel20;
  private JLabel jLabel21;
  private JLabel jLabel22;
  private JLabel jLabel23;
  private JLabel jLabel24;
  private JLabel jLabel25;
  private JLabel jLabel26;
  private JLabel jLabel27;
  private JLabel jLabel28;
  private JLabel jLabel29;
  private JLabel jLabel3;
  private JLabel jLabel30;
  private JLabel jLabel31;
  private JLabel jLabel32;
  private JLabel jLabel33;
  private JLabel jLabel34;
  private JLabel jLabel35;
  private JLabel jLabel36;
  private JLabel jLabel37;
  private JLabel jLabel38;
  private JLabel jLabel39;
  private JLabel jLabel4;
  private JLabel jLabel40;
  private JLabel jLabel41;
  private JLabel jLabel42;
  private JLabel jLabel43;
  private JLabel jLabel44;
  private JLabel jLabel45;
  private JLabel jLabel46;
  private JLabel jLabel47;
  private JLabel jLabel48;
  private JLabel jLabel49;
  private JLabel jLabel5;
  private JLabel jLabel50;
  private JLabel jLabel51;
  private JLabel jLabel52;
  private JLabel jLabel53;
  private JLabel jLabel54;
  private JLabel jLabel55;
  private JLabel jLabel56;
  private JLabel jLabel57;
  private JLabel jLabel58;
  private JLabel jLabel6;
  private JLabel jLabel7;
  private JLabel jLabel8;
  private JLabel jLabel9;
  private JMenu jMenu1;
  private JMenu jMenu2;
  private JMenu jMenu3;
  private JMenu jMenu4;
  private JMenu jMenu5;
  private JMenu jMenu6;
  private JMenuItem jMenuItem1;
  private JMenuItem jMenuItem2;
  private JMenuItem jMenuItem3;
  private JPanel jPanel1;
  private JPanel jPanel10;
  private JPanel jPanel2;
  private JPanel jPanel3;
  private JPanel jPanel4;
  private JPanel jPanel5;
  private JPanel jPanel6;
  private JPanel jPanel7;
  private JPanel jPanel9;
  private JScrollPane jScrollPane1;
  private JScrollPane jScrollPane10;
  private JScrollPane jScrollPane11;
  private JScrollPane jScrollPane12;
  private JScrollPane jScrollPane2;
  private JScrollPane jScrollPane3;
  private JScrollPane jScrollPane4;
  private JScrollPane jScrollPane5;
  private JScrollPane jScrollPane6;
  private JScrollPane jScrollPane7;
  private JScrollPane jScrollPane8;
  private JScrollPane jScrollPane9;
  private JToolBar.Separator jSeparator2;
  private JToolBar.Separator jSeparator3;
  private JToolBar.Separator jSeparator4;
  private JTextField jTextField1;
  private JTextField jTextField2;
  private JToolBar jToolBar1;
  private JPopupMenu jobPopUp;
  private JComboBox mapReduceSIMComboBox;
  private JLabel memoryUsageLabel;
  private JTextField nameTextField;
  private Panel panel1;
  private JLabel queueInfoLabel;
  private JMenuItem refreshMenu;
  private JPopupMenu refreshPopup;
  private JButton removeIPTableButton;
  private JTextField rmiSuffixTextField;
  private JTextField startTimeTextField;
  private JTextField statusTextField;
  private JButton systemMonitorButton;
  private JDialog systemMonitorDialog;
  private JDialog systemMonitorViewDialog;
  private JLabel systemTimeLabel;
  private JTextField taskNameTextField;
  private JPopupMenu taskPopUp;
  private JPopupMenu taskPopUpForMS;
  private JPopupMenu userPopUp;
  private JButton viewSystemMonitorButton;

  public iesMainFrm()
    throws ParserConfigurationException, IOException, ExecutionTerminationException
  {
    setLooknFeel();
    initComponents();
    initData();
    setTaskUsers();
    initTree();
    initFrames();
    this.player = new TaskStatePlayer(this.doo);
    this.player.start();
  }

  public iesMainFrm(String userName, long sessionId, boolean viewAllUser) throws ParserConfigurationException, IOException, ExecutionTerminationException
  {
    System.out.println("start");
    setLooknFeel();
    initComponents();
    initData();
    setTaskUsers();
    initTree();
    initFrames();
    this.player = new TaskStatePlayer(this.doo);
    this.player.start();
    this.loginedName = userName;
    this.sessionId = sessionId;
    this.viewAllUser = viewAllUser;
  }

  public JTree getTree()
  {
    return this.NaviTree;
  }

  private void initFrames()
  {
    this.Controllers.add(this.DBMainList, "Center");
    this.cur_view = this.DBMainList;
    this.cur_view.setVisible(true);
  }

  private void initData()
    throws ParserConfigurationException, IOException
  {
    Servers.SetServers();
    this.ol = iesRMI.getDOFromRMI();
    this.tl = iesRMI.getTPOFromRMI();
    this.doo = iesRMI.getDeployerFromRMI();
    this.ol.initDocumentTree("config/DataDefinition.xml");
    this.tl.initDocument("config/task.xml");
    this.taskId_list = new Vector();
    this.ipTableTasks = iesRMI.getIPTableFromRMI();
    this.ipTableTasks.initDocument();

    this.templateOper = iesRMI.getTemplateOperationFromRMI();
  }

  public long getDiffTime(PropertyOperation master, PropertyOperation slave) throws RemoteException {
    return master.getCurrentSystemTime() - slave.getCurrentSystemTime();
  }

  public void startSystemInfoTimer()
  {
    if (!this.onSystemInfoTimer) {
      this.systemInfoTimer = new Timer();
      SystemInfoTimerTask systemInfoTimerTask = new SystemInfoTimerTask();
      this.systemInfoTimer.schedule(systemInfoTimerTask, 0L, 1000L);
      this.onSystemInfoTimer = true;
    }
  }

  public void closeSystemInfoTimer() throws RemoteException {
    if (this.onSystemInfoTimer == true) {
      this.systemInfoTimer.cancel();
      this.onSystemInfoTimer = false;
    }
  }

  public void startExportSystemInfoTimer(long taskID)
  {
    Timer exportSystemInfoTimer = new Timer();
    ExportSystemInfoTimerTask exportSystemInfoTimerTask = new ExportSystemInfoTimerTask();
    exportSystemInfoTimerTask.setExportRMI();
    exportSystemInfoTimerTask.cur_TaskID = taskID;
    this.exportSystemInfoTimerList.put(Long.valueOf(taskID), exportSystemInfoTimer);
    exportSystemInfoTimer.schedule(exportSystemInfoTimerTask, 0L, 1000L);
  }

  public void closeExportSystemInfoTimer(long taskID) {
    this.countCpuUsage = 0;
    ((Timer)this.exportSystemInfoTimerList.get(Long.valueOf(taskID))).cancel();
    this.exportSystemInfoTimerList.remove(Long.valueOf(taskID));
  }

  public void initTree()
    throws RemoteException, ExecutionTerminationException
  {
    DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(new NodeInfo("IES", "db"));
    DefaultMutableTreeNode ddNode = new DefaultMutableTreeNode(new NodeInfo("Data Definition", "dd"));

    Vector userList = this.ol.getUserList();

    this.new_ddl_id = -1;

    boolean user_exist = false;

    for (int i = 0; i < userList.size(); i++)
    {
      if (((String)userList.get(i)).equals(this.loginedName))
      {
        DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(new NodeInfo((String)userList.get(i), "user"));
        ddNode.add(userNode);

        Vector ddlList = this.ol.getDataDefinitionList((String)userList.get(i));

        for (int j = 0; j < ddlList.size(); j++)
        {
          DefaultMutableTreeNode ddlNode = new DefaultMutableTreeNode(new NodeInfo(((DataDefinition)ddlList.get(j)).getTitle(), "ddl", ((DataDefinition)ddlList.get(j)).getId()));
          userNode.add(ddlNode);
          if (((DataDefinition)ddlList.get(j)).getId() > this.new_ddl_id)
          {
            this.new_ddl_id = ((DataDefinition)ddlList.get(j)).getId();
          }

        }

        user_exist = true;

        break;
      }

    }

    if (!user_exist)
    {
      DefaultMutableTreeNode userNode = new DefaultMutableTreeNode(new NodeInfo(this.loginedName, "user"));
      ddNode.add(userNode);
    }

    user_exist = false;

    this.new_ddl_id += 1;

    DefaultMutableTreeNode DataDefNode = new DefaultMutableTreeNode(new NodeInfo("Tasks", "tasks"));
    DefaultMutableTreeNode UserNode = new DefaultMutableTreeNode(new NodeInfo("User", "ddl"));
    DefaultMutableTreeNode TaskNode = new DefaultMutableTreeNode(new NodeInfo("View", "task_mining"));
    DefaultMutableTreeNode JobNode = new DefaultMutableTreeNode(new NodeInfo("Job", "job_basestream"));
    Vector OwnerList = this.tl.getOwnerList();
    NodeInfo[] TaskXmlList = null;

    NodeInfo[] XmlJobList = null;
    for (int i = 0; i < OwnerList.size(); i++)
    {
      if (((String)OwnerList.get(i)).equals(this.loginedName))
      {
        UserNode = new DefaultMutableTreeNode(new NodeInfo((String)OwnerList.get(i), "taskuser"));

        TaskXmlList = this.tl.getTaskList((String)OwnerList.get(i));

        for (int j = 0; j < TaskXmlList.length; j++)
        {
          TaskNode = addTask(TaskXmlList[j]);
          if ((this.doo.currentState(TaskXmlList[j].getLongId()) == TaskState.RUNNING) ||
            (this.doo
            .currentState(TaskXmlList[j]
            .getLongId()) == TaskState.PAUSE))
          {
            addRunTaskId(TaskXmlList[j].getLongId());
            new TaskExecutionMonitor(TaskXmlList[j].getLongId()).start();
          }
          if (!this.tl.getTaskHidden(TaskXmlList[j].getLongId())) {
            XmlJobList = this.tl.getJobList(TaskXmlList[j].getLongId());
            for (int k = 0; k < XmlJobList.length; k++)
            {
              JobNode = addJob(XmlJobList[k]);
              TaskNode.add(JobNode);
            }
          }
          UserNode.add(TaskNode);
        }
        DataDefNode.add(UserNode);

        user_exist = true;
        break;
      }

    }

    if (!user_exist)
    {
      UserNode = new DefaultMutableTreeNode(new NodeInfo(this.loginedName, "taskuser"));
      DataDefNode.add(UserNode);
    }

    DefaultMutableTreeNode IP_Table = new DefaultMutableTreeNode(new NodeInfo("IP Table", "iptable"));
    ipTableTaskInfo[] tasks = this.ipTableTasks.getAllTaskInfos();
    for (int i = 0; i < tasks.length; i++)
    {
      DefaultMutableTreeNode task = new DefaultMutableTreeNode(new NodeInfo(tasks[i].getName(), "iptabletask", tasks[i].getId()));
      IP_Table.add(task);
    }

    DefaultMutableTreeNode PipesNode = new DefaultMutableTreeNode(new NodeInfo("Data Pipe", "pipes"));

    rootNode.add(ddNode);
    rootNode.add(DataDefNode);
    rootNode.add(IP_Table);
    rootNode.add(PipesNode);
    this.NaviTree.setModel(new DefaultTreeModel(rootNode));

    this.NaviTree.setCellRenderer(new NaviRenderer());
  }

  public void addRunTaskId(long taskId)
  {
    if (!this.taskId_list.contains(Long.valueOf(taskId)))
    {
      this.taskId_list.add(Long.valueOf(taskId));
    }
  }

  private void setLooknFeel()
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void setTaskUsers() throws RemoteException
  {
    Vector Tasks = this.tl.getOwnerList();
    DefaultTableModel UserListModel = (DefaultTableModel)this.TaskListTable.getModel();
    for (int i = UserListModel.getRowCount() - 1; i >= 0; i--)
    {
      UserListModel.removeRow(i);
    }
    this.TaskModelList = new DefaultTableModel[Tasks.size()];
    for (int i = 0; i < Tasks.size(); i++)
    {
      if (((String)Tasks.get(i)).equals(this.loginedName))
      {
        String[] Task = new String[2];
        Task[0] = ((String)Tasks.get(i));
        Task[1] = "Folder";
        UserListModel.addRow(Task);

        break;
      }
    }
  }

  private DefaultTableModel setTasks(String owner)
    throws RemoteException
  {
    DefaultTableModel CurrentTaskModel = new DefaultTableModel(new String[] { "Name", "Type" }, 0);

    NodeInfo[] TaskElementList = this.tl.getTaskList(owner);
    for (int i = 0; i < TaskElementList.length; i++)
    {
      CurrentTaskModel.addRow(new String[] { TaskElementList[i]
        .getName(), "Task" });
    }

    return CurrentTaskModel;
  }

  public void setTreeData()
    throws RemoteException
  {
    DefaultMutableTreeNode RootNode = new DefaultMutableTreeNode(new NodeInfo("dblab", "user"));
    DefaultMutableTreeNode DataDefNode = new DefaultMutableTreeNode(new NodeInfo("Data Definition", "dd"));
    DefaultMutableTreeNode UserNode = new DefaultMutableTreeNode(new NodeInfo("User", "ddl"));
    DefaultMutableTreeNode TaskNode = new DefaultMutableTreeNode(new NodeInfo("View", "task_mining"));
    DefaultMutableTreeNode JobNode = new DefaultMutableTreeNode(new NodeInfo("Job", "job_basestream"));
    DataDefNode.add(UserNode);
    RootNode.add(DataDefNode);

    DataDefNode = new DefaultMutableTreeNode(new NodeInfo("Tasks", "tasks"));
    Vector OwnerList = this.tl.getOwnerList();
    NodeInfo[] TaskXmlList = null;

    NodeInfo[] XmlJobList = null;
    for (int i = 0; i < OwnerList.size(); i++)
    {
      UserNode = new DefaultMutableTreeNode(new NodeInfo((String)OwnerList.get(i), "taskuser"));

      TaskXmlList = this.tl.getTaskList((String)OwnerList.get(i));

      for (int j = 0; j < TaskXmlList.length; j++)
      {
        TaskNode = addTask(TaskXmlList[j]);
        XmlJobList = this.tl.getJobList(TaskXmlList[j].getLongId());
        for (int k = 0; k < XmlJobList.length; k++)
        {
          JobNode = addJob(XmlJobList[k]);
          TaskNode.add(JobNode);
        }
        UserNode.add(TaskNode);
      }
      DataDefNode.add(UserNode);
    }
    RootNode.add(DataDefNode);
    this.NaviTree.setModel(new DefaultTreeModel(RootNode));

    this.NaviTree.setCellRenderer(new NaviRenderer());
  }

  private void initTaskList(String user) throws RemoteException
  {
    NodeInfo[] jList = this.tl.getTaskList(user);
    DefaultTableModel table = (DefaultTableModel)this.TaskUserListTable.getModel();
    cleanTable(table);

    for (int i = 0; i < jList.length; i++)
    {
      table.addRow(new Object[] { jList[i]
        .getName(), jList[i].getType() });
    }
  }

  public void warnMsgDlg(Component component, String msg)
  {
    JOptionPane.showMessageDialog(component, msg);
  }

  private DefaultTableModel cleanTable(DefaultTableModel table)
  {
    for (int i = table.getRowCount() - 1; i >= 0; i--)
    {
      table.removeRow(i);
    }

    return table;
  }

  private void setAttributeTable(DataDefinition ddl, JTable table)
  {
    DefaultTableModel model = (DefaultTableModel)table.getModel();
    model = cleanTable(model);

    Vector atrList = ddl.getAttribute();

    for (int i = 0; i < atrList.size(); i++)
    {
      if (((Attribute)atrList.get(i)).getType().equals("string"))
      {
        model.insertRow(i, new Object[] {
          Integer.valueOf(i + 1),
          ((Attribute)atrList.get(i)).getName(), ((Attribute)atrList.get(i)).getType(), null, Boolean.valueOf(((Attribute)atrList.get(i)).getAllowNulls()), ((Attribute)atrList.get(i)).getDefaultValue() });
      }
      else if (((Attribute)atrList.get(i)).getType().equals("integer"))
      {
        model.insertRow(i, new Object[] {
          Integer.valueOf(i + 1),
          ((Attribute)atrList.get(i)).getName(), ((Attribute)atrList.get(i)).getType(), Integer.valueOf(this.sizeOfInteger), Boolean.valueOf(((Attribute)atrList.get(i)).getAllowNulls()), ((Attribute)atrList.get(i)).getDefaultValue() });
      }
      else if (((Attribute)atrList.get(i)).getType().equals("double"))
      {
        model.insertRow(i, new Object[] {
          Integer.valueOf(i + 1),
          ((Attribute)atrList.get(i)).getName(), ((Attribute)atrList.get(i)).getType(), Integer.valueOf(this.sizeOfDouble), Boolean.valueOf(((Attribute)atrList.get(i)).getAllowNulls()), ((Attribute)atrList.get(i)).getDefaultValue() });
      }
      else if (((Attribute)atrList.get(i)).getType().equals("long"))
      {
        model.insertRow(i, new Object[] {
          Integer.valueOf(i + 1),
          ((Attribute)atrList.get(i)).getName(), ((Attribute)atrList.get(i)).getType(), Integer.valueOf(this.sizeOfLong), Boolean.valueOf(((Attribute)atrList.get(i)).getAllowNulls()), ((Attribute)atrList.get(i)).getDefaultValue() });
      }
    }
  }

  private void setFixedTable(DataDefinition ddl, JTable table)
  {
    DefaultTableModel model = (DefaultTableModel)table.getModel();
    model = cleanTable(model);

    Vector atrList = ddl.getAttribute();
    Vector fixList = ddl.getFixedField();

    int i = 0;
    if (fixList == null)
    {
      return;
    }

    for (; i < fixList.size(); i++)
    {
      model.insertRow(i, new Object[] {
        Integer.valueOf(i + 1),
        ((Attribute)atrList.get(i)).getName(), ((Attribute)atrList.get(i)).getType(), Integer.valueOf(((FixedPosition)fixList.get(i)).getOffset()), Integer.valueOf(((FixedPosition)fixList.get(i)).getLength()) });
    }

    for (; i < atrList.size(); i++)
    {
      this.current_ddl.getFixedField().add(new FixedPosition(i + 1, 0, 0));
      model.insertRow(i, new Object[] {
        Integer.valueOf(i + 1),
        ((Attribute)atrList.get(i)).getName(), ((Attribute)atrList.get(i)).getType(), Integer.valueOf(0), Integer.valueOf(0) });
    }
  }

  private void setItemsetTable(DataDefinition ddl, JTable table)
  {
    DefaultTableModel model = (DefaultTableModel)table.getModel();
    model = cleanTable(model);

    Vector atrList = ddl.getAttribute();
    int tid = ddl.getItemsetTidField();
    Vector itemset = ddl.getItemsetField();

    if (itemset == null)
    {
      itemset = new Vector();
    }

    if (atrList != null)
    {
      for (int i = 0; i < atrList.size(); i++)
      {
        boolean isTid = false;
        boolean isItemset = false;

        if (i + 1 == tid)
        {
          isTid = true;
        }

        if (itemset.contains(Integer.valueOf(i + 1)))
        {
          isItemset = true;
        }

        model.insertRow(i, new Object[] {
          Integer.valueOf(i + 1),
          ((Attribute)atrList.get(i)).getName(), Boolean.valueOf(isTid), Boolean.valueOf(isItemset) });
      }
    }
  }

  private void initUserList()
    throws RemoteException
  {
    Vector userList = this.ol.getUserList();

    DefaultTableModel userTable = (DefaultTableModel)this.UserListTable.getModel();

    userTable = cleanTable(userTable);

    for (int i = 0; i < userList.size(); i++)
    {
      if (((String)userList.get(i)).equals(this.loginedName))
      {
        userTable.addRow(new Object[] { userList
          .get(i),
          "Folder" });

        break;
      }
    }
  }

  private void initDDLList(String user)
    throws RemoteException
  {
    Vector DDLListt = this.ol.getDataDefinitionList(user);

    DefaultTableModel ddlTable = (DefaultTableModel)this.DDLListTable.getModel();

    ddlTable = cleanTable(ddlTable);

    int temp = ddlTable.getRowCount();

    for (int i = 0; i < DDLListt.size(); i++)
    {
      ddlTable.insertRow(i, new Object[] {
        ((DataDefinition)DDLListt
        .get(i))
        .getTitle(), "DDL", ((DataDefinition)DDLListt.get(i)).getComments() });
    }
  }

  private void initDDLDetail(int id)
    throws RemoteException
  {
    this.current_ddl = this.ol.getDataDefintion(id);

    this.DDLDetailTab.setSelectedComponent(this.DDLStructure);

    initDDLDetailTab1(this.current_ddl);
    initDDLDetailTab2(this.current_ddl);
    initDDLDetailTab3(this.current_ddl);
  }

  private void initDDLDetailTab1(DataDefinition ddl)
  {
    this.DDLStrName.setText(ddl.getTitle());
    this.DDLStrComment.setText(ddl.getComments());

    if (ddl.getVariableDeliType())
    {
      this.DDLStrType.setText("Variable");
      this.DDLStrRecordDel.setText('[' + ddl.getRecordDelimeter() + ']');
      this.DDLStrFieldDel.setText('[' + ddl.getFieldDelimeter() + ']');

      this.DDLStrSetting.remove(this.DDLStrFixedPanel);
      this.DDLStrFixedPanel.setVisible(false);
      this.DDLStrVariablePanel.setVisible(true);
      this.DDLStrSetting.add(this.DDLStrVariablePanel, "Center");
    }
    else
    {
      this.DDLStrType.setText("Fixed");
      setFixedTable(ddl, this.DDLStrFixedTable);

      this.DDLStrSetting.remove(this.DDLStrVariablePanel);
      this.DDLStrVariablePanel.setVisible(false);
      this.DDLStrFixedPanel.setVisible(true);
      this.DDLStrSetting.add(this.DDLStrFixedPanel, "Center");
    }

    setAttributeTable(ddl, this.DDLStrTable);
  }

  private void initDDLDetailTab2(DataDefinition ddl)
  {
    this.DDLDefName.setText(ddl.getTitle());
    this.DDLDefComment.setText(ddl.getComments());

    Date date = new Date(ddl.getCreationDate());
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    this.DDLDefDate.setText(format.format(date));

    if (ddl.getVariableDeliType())
    {
      this.FieldTypeVariable.setSelected(true);
    }
    else
    {
      this.FieldTypeFixed.setSelected(true);
    }

    this.old_attributes = ddl.getAttribute().size();

    setAttributeTable(ddl, this.DDLDefTable);
  }

  private void initDDLDetailTab3(DataDefinition ddl)
  {
  }

  private void initVariableFrame()
  {
    this.VFFieldDelimeter.setText(this.current_ddl.getFieldDelimeter());
    this.VFRecordDelimeter.setText(this.current_ddl.getRecordDelimeter());
    this.UseItemsetCheck.setSelected(this.current_ddl.isVariableItemsetUse);

    setItemsetTable(this.current_ddl, this.ItemsetTable);

    if (this.current_ddl.isVariableItemsetUse)
    {
      this.ItemsetDelimeter.setText(this.current_ddl.getItemsetDelimeter());

      if (this.current_ddl.getFieldDelimeter().equals(this.current_ddl.getItemsetDelimeter()))
      {
        this.ItemsetSame.setSelected(true);
        this.ItemsetSame.setEnabled(true);
        this.ItemsetDifferent.setEnabled(true);
        this.ItemsetTableScroll.setEnabled(true);
        this.ItemsetTable.setEnabled(true);
        this.ItemsetDelimeter.setEnabled(false);
      }
      else
      {
        this.ItemsetDifferent.setSelected(true);
        this.ItemsetSame.setEnabled(true);
        this.ItemsetDifferent.setEnabled(true);
        this.ItemsetTableScroll.setEnabled(true);
        this.ItemsetTable.setEnabled(true);
        this.ItemsetDelimeter.setEnabled(true);
      }
    }
    else
    {
      this.ItemsetDelimeter.setText(this.current_ddl.getFieldDelimeter());
      this.ItemsetDifferent.setSelected(true);
      this.ItemsetSame.setEnabled(false);
      this.ItemsetDifferent.setEnabled(false);
      this.ItemsetTableScroll.setEnabled(false);
      this.ItemsetTable.setEnabled(false);
    }
  }

  private void initFixedFrame()
  {
    if (this.current_ddl.getFixedField() == null)
    {
      this.current_ddl.setFixedField(new Vector());
    }

    if (this.current_ddl.fixedField.size() == 0)
    {
      if (this.current_ddl.getAttribute() == null)
      {
        this.current_ddl.setAttribute(new Vector());
      }

      Vector atrs = this.current_ddl.getAttribute();

      for (int i = 0; i < atrs.size(); i++)
      {
        this.current_ddl.fixedField.add(new FixedPosition(((Attribute)atrs.get(i)).getId(), 0, 0));
      }
    }

    setFixedTable(this.current_ddl, this.FixedTable);
  }

  private void initFieldAddFrame()
  {
    this.FieldAddFrameName.setText(this.current_ddl.getOwner());
    this.FieldAddName.setText("");
    this.FieldAddType.setSelectedIndex(0);
    this.FieldAddNull.setSelected(false);
    this.FieldAddDefault.setText("");
  }

  private void initDDLCreateDialog(String username)
  {
    this.CreateOwnerName.setText(username);
    this.DDLCreateName.setText("");
    this.DDLCreateComment.setText("");
    this.CreateFieldTypeVariable.setSelected(true);
    cleanTable((DefaultTableModel)this.DDLCreateTable.getModel());
    this.old_ddl = this.current_ddl;
    this.current_ddl = new DataDefinition();

    initVariableFrame();
    initFixedFrame();

    this.current_ddl = this.old_ddl;
  }

  private boolean checkAttributeTable()
  {
    DefaultTableModel model = (DefaultTableModel)this.DDLDefTable.getModel();

    if (this.create_mode)
    {
      model = (DefaultTableModel)this.DDLCreateTable.getModel();
    }

    if ((model.getRowCount() == 0) && (this.DDLCreateDialog.isVisible()))
    {
      if (this.create_mode)
      {
        warnMsgDlg(this.DDLCreateDialog, "Data Definition must have at least 1 attribute");
      }
      else
      {
        warnMsgDlg(this.DDLDetail, "Data Definition must have at least 1 attribute");
      }
      return true;
    }

    for (int i = 0; i < model.getRowCount() - 1; i++)
    {
      for (int j = i + 1; j < model.getRowCount(); j++)
      {
        if (model.getValueAt(i, 1).equals(model.getValueAt(j, 1)))
        {
          if (this.create_mode)
          {
            warnMsgDlg(this.DDLCreateDialog, "'" + model.getValueAt(i, 1) + "' is used more than 1 time");
          }
          else
          {
            warnMsgDlg(this.DDLDetail, "'" + model.getValueAt(i, 1) + "' is used more than 1 time");
          }
          return true;
        }
      }
    }

    for (int i = 0; i < model.getRowCount(); i++)
    {
      ((Attribute)this.current_ddl.attribute.get(i)).setId(i + 1);
      ((Attribute)this.current_ddl.attribute.get(i)).name = String.valueOf(model.getValueAt(i, 1));
      ((Attribute)this.current_ddl.attribute.get(i)).type = String.valueOf(model.getValueAt(i, 2));

      if (model.getValueAt(i, 4).equals(Boolean.valueOf(true)))
      {
        ((Attribute)this.current_ddl.attribute.get(i)).allowNulls = true;
      }
      else
      {
        ((Attribute)this.current_ddl.attribute.get(i)).allowNulls = false;
      }
      ((Attribute)this.current_ddl.attribute.get(i)).defaultValue = String.valueOf(model.getValueAt(i, 5));
    }

    return false;
  }

  private void TreePopup(MouseEvent evt)
    throws RemoteException, ExecutionTerminationException
  {
    if (evt.isPopupTrigger())
    {
      TreePath path = this.NaviTree.getPathForLocation(evt.getPoint().x, evt.getPoint().y);

      if (path == null)
      {
        this.refreshPopup.show(evt.getComponent(), evt.getX(), evt.getY());
        return;
      }

      DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();

      if (node == null)
      {
        return;
      }

      NodeInfo type = (NodeInfo)node.getUserObject();

      this.popnode = type;
      this.datanode = node;

      if (type.getType().equals("user"))
      {
        this.PopupMenu2.show(evt.getComponent(), evt.getX(), evt.getY());
      }
      else if (type.getType().equals("ddl"))
      {
        this.PopupMenu1.show(evt.getComponent(), evt.getX(), evt.getY());
      }
      else if (type.getType().equals("task"))
      {
        if (this.tl.getTaskType(this.popnode.getLongId()).equals("DataStreamQueryProcessing"))
        {
          this.taskPopUpForMS.show(evt.getComponent(), evt.getX(), evt.getY());
        }
        else
        {
          this.taskPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
        }
        switchTaskPopupText(this.doo.currentState(this.popnode.getLongId()));
        if (this.tl.getTaskPreloadFlag(this.popnode.getLongId()).equals("true")) {
          this.item_preloadTask.setText("Preload Disable");
          this.item_preloadTask.setIcon(new ImageIcon(getClass().getResource("/dbies/imgs/check.gif")));
        } else {
          this.item_preloadTask.setText("Preload Enable");
          this.item_preloadTask.setIcon(null);
        }
      }
      else if (type.getType().equals("job"))
      {
        DefaultMutableTreeNode pNode = (DefaultMutableTreeNode)node.getParent();

        NodeInfo parent = (NodeInfo)pNode.getUserObject();

        if (this.tl.getTaskType(parent.getLongId()).equals("DataStreamQueryProcessing"))
        {
          this.item_removeJob.setEnabled(false);
        }
        else
        {
          this.item_removeJob.setEnabled(true);
        }

        this.jobPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      }
      else if (type.getType().equals("taskuser"))
      {
        this.userPopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      }
      else if (type.getType().equals("iptable"))
      {
        this.ipTablePopUp.show(evt.getComponent(), evt.getX(), evt.getY());
      }
    }
  }

  private boolean checkVariableValidity()
  {
    if (this.VFFieldDelimeter.getText().length() == 0)
    {
      warnMsgDlg(this.VariableDialog, "You must input Field Delimeter");
      return true;
    }

    if (this.VFRecordDelimeter.getText().length() == 0)
    {
      warnMsgDlg(this.VariableDialog, "You must input Record Delimeter");
      return true;
    }

    if (this.VFFieldDelimeter.getText().equals(this.VFRecordDelimeter.getText()))
    {
      warnMsgDlg(this.VariableDialog, "Field Delimeter must be different from Record Delimeter");
      return true;
    }

    if (this.UseItemsetCheck.isSelected())
    {
      if ((this.ItemsetDifferent.isSelected()) && (this.ItemsetDelimeter.getText().equals(this.VFRecordDelimeter.getText())))
      {
        warnMsgDlg(this.VariableDialog, "Itemset Delimeter must be different from Record Delimeter");
        return true;
      }

      if ((this.ItemsetDifferent.isSelected()) && (this.ItemsetDelimeter.getText().equals(this.VFFieldDelimeter.getText())))
      {
        warnMsgDlg(this.VariableDialog, "Itemset Delimeter must be different from Field Delimeter");
        return true;
      }

      DefaultTableModel model = (DefaultTableModel)this.ItemsetTable.getModel();

      Vector itemset = new Vector();

      if (this.ItemsetDifferent.isSelected())
      {
        int tid = -1;

        for (int i = 0; i < model.getRowCount(); i++)
        {
          if (model.getValueAt(i, 2).equals(Boolean.valueOf(true)))
          {
            if (model.getValueAt(i, 3).equals(Boolean.valueOf(true)))
            {
              warnMsgDlg(this.VariableDialog, "You can't choose 1 Attribute as TID and Itemset at same time");
              return true;
            }

            if (tid < 0)
            {
              tid = i + 1;
            }
            else
            {
              warnMsgDlg(this.VariableDialog, "You must choose 1 TID Attribute");
              return true;
            }
          }
        }

        if (tid < 0)
        {
          warnMsgDlg(this.VariableDialog, "You must choose 1 TID Attribute");
          return true;
        }
      }
      else
      {
        if (model.getRowCount() != 2)
        {
          warnMsgDlg(this.VariableDialog, "You can use only 2 attributes in this case");
          return true;
        }

        if (((model.getValueAt(0, 2).equals(Boolean.valueOf(true))) && (model.getValueAt(0, 3).equals(Boolean.valueOf(true)))) || (
          (model
          .getValueAt(1, 2)
          .equals(Boolean.valueOf(true))) && (model.getValueAt(1, 3).equals(Boolean.valueOf(true)))))
        {
          warnMsgDlg(this.VariableDialog, "You can't choose 1 Attribute as TID and Itemset at same time");
          return true;
        }

        if ((model.getValueAt(0, 2).equals(Boolean.valueOf(false))) && (model.getValueAt(1, 2).equals(Boolean.valueOf(false))))
        {
          warnMsgDlg(this.VariableDialog, "You must choose 1 TID Attribute");
          return true;
        }

        if ((model.getValueAt(0, 3).equals(Boolean.valueOf(false))) && (model.getValueAt(1, 3).equals(Boolean.valueOf(false))))
        {
          warnMsgDlg(this.VariableDialog, "You must choose 1 Itemset Attribute");
          return true;
        }
      }
    }

    return false;
  }

  private boolean checkFixedValidity()
  {
    DefaultTableModel model = (DefaultTableModel)this.FixedTable.getModel();

    for (int i = 0; i < model.getRowCount() - 1; i++)
    {
      int begin = Integer.parseInt(model.getValueAt(i, 3).toString());
      int end = begin + Integer.parseInt(model.getValueAt(i, 4).toString());
      int nEnd;
      for (int j = 1; j < model.getRowCount(); j++)
      {
        int nBegin = Integer.parseInt(model.getValueAt(j, 3).toString());
        nEnd = nBegin + Integer.parseInt(model.getValueAt(j, 4).toString());
      }

    }

    return false;
  }

  private boolean checkAttributeValidity()
  {
    DefaultTableModel model = (DefaultTableModel)this.DDLDefTable.getModel();

    if (this.create_mode)
    {
      model = (DefaultTableModel)this.DDLCreateTable.getModel();
    }

    if (this.FieldAddName.getText().trim().length() == 0)
    {
      warnMsgDlg(this.FieldAddDialog, "You must input attribute name");
      return true;
    }

    for (int i = 0; i < model.getRowCount(); i++)
    {
      if (model.getValueAt(i, 1).equals(this.FieldAddName.getText()))
      {
        warnMsgDlg(this.FieldAddDialog, "'" + this.FieldAddName.getText() + "' is already used name in this table");
        return true;
      }
    }

    return false;
  }

  private boolean confirmVariable()
  {
    if (checkVariableValidity())
    {
      return false;
    }

    this.current_ddl.setFieldDelimeter(this.VFFieldDelimeter.getText());
    this.current_ddl.setRecordDelimeter(this.VFRecordDelimeter.getText());
    this.current_ddl.setIsVariableItemsetUse(this.UseItemsetCheck.isSelected());
    if (this.ItemsetDifferent.isSelected())
    {
      this.current_ddl.setItemsetDelimeter(this.ItemsetDelimeter.getText());
    }
    else
    {
      this.current_ddl.setItemsetDelimeter(this.VFFieldDelimeter.getText());
    }

    DefaultTableModel model = (DefaultTableModel)this.ItemsetTable.getModel();

    int tid = -1;
    Vector itemset = new Vector();

    for (int i = 0; i < model.getRowCount(); i++)
    {
      if (model.getValueAt(i, 2).equals(Boolean.valueOf(true)))
      {
        tid = i + 1;
      }
      else if (model.getValueAt(i, 3).equals(Boolean.valueOf(true)))
      {
        itemset.add(Integer.valueOf(i + 1));
      }

    }

    if (tid > 0)
    {
      this.current_ddl.setItemsetTidField(tid);
    }

    this.current_ddl.setItemsetField(itemset);

    return true;
  }

  private boolean confirmFixed()
  {
    if (checkFixedValidity())
    {
      return false;
    }

    DefaultTableModel model = (DefaultTableModel)this.FixedTable.getModel();

    Vector fixedP = new Vector();

    for (int i = 0; i < model.getRowCount(); i++)
    {
      fixedP.add(new FixedPosition(String.valueOf(i + 1), String.valueOf(model.getValueAt(i, 3)), String.valueOf(model.getValueAt(i, 4))));
    }

    this.current_ddl.setFixedField(fixedP);
    return true;
  }

  private boolean confirmFieldAddFrame()
  {
    if (checkAttributeValidity())
    {
      return false;
    }

    if (this.current_ddl.getAttribute() == null)
    {
      this.current_ddl.setAttribute(new Vector());
    }

    DefaultTableModel model = (DefaultTableModel)this.DDLDefTable.getModel();

    if (this.create_mode)
    {
      model = (DefaultTableModel)this.DDLCreateTable.getModel();
    }

    Attribute newAtr = new Attribute(model
      .getRowCount() + 1, this.FieldAddName
      .getText(),
      String.valueOf(this.FieldAddType
      .getSelectedItem()), this.FieldAddNull
      .isSelected(), this.FieldAddDefault
      .getText());

    this.current_ddl.attribute.add(newAtr);

    if (this.create_mode)
    {
      setAttributeTable(this.current_ddl, this.DDLCreateTable);
    }
    else
    {
      setAttributeTable(this.current_ddl, this.DDLDefTable);
    }

    initVariableFrame();
    initFixedFrame();

    return true;
  }

  private void delAttributeField()
  {
    int[] rows = this.DDLDefTable.getSelectedRows();

    if (this.create_mode)
    {
      rows = this.DDLCreateTable.getSelectedRows();
    }

    for (int i = rows.length - 1; i >= 0; i--)
    {
      this.current_ddl.attribute.remove(rows[i]);
      this.current_ddl.fixedField.remove(rows[i]);
    }

    initVariableFrame();
    initFixedFrame();

    for (int i = 0; i < this.current_ddl.attribute.size(); i++)
    {
      ((FixedPosition)this.current_ddl.fixedField.get(i)).setId(i + 1);
      ((Attribute)this.current_ddl.attribute.get(i)).setId(i + 1);
    }

    if (this.create_mode)
    {
      setAttributeTable(this.current_ddl, this.DDLCreateTable);
    }
    else
    {
      setAttributeTable(this.current_ddl, this.DDLDefTable);
    }
  }

  private boolean commitNewDDL()
    throws TransformerConfigurationException, TransformerException, RemoteException
  {
    if (this.DDLCreateName.getText().trim().isEmpty())
    {
      warnMsgDlg(this.DDLCreateDialog, "You must input DDL's name");
      return false;
    }

    if (checkAttributeTable())
    {
      return false;
    }

    if (this.current_ddl == null)
    {
      this.current_ddl = new DataDefinition();
    }

    if (this.CreateFieldTypeVariable.isSelected())
    {
      if (checkVariableValidity())
      {
        return false;
      }

      this.current_ddl.setVariableDeliType(true);
    }
    else
    {
      if (checkFixedValidity())
      {
        return false;
      }

      this.current_ddl.setVariableDeliType(false);
    }

    this.current_ddl.setId(this.new_ddl_id);
    this.current_ddl.setTitle(this.DDLCreateName.getText());
    this.current_ddl.setOwner(this.CreateOwnerName.getText());
    this.current_ddl.setCreationDate(System.currentTimeMillis());
    this.current_ddl.setComments(this.DDLCreateComment.getText());

    int c_id = this.ol.createDataDefintion(this.current_ddl);

    initUserList();
    initDDLList(this.CreateOwnerName.getText());

    NodeInfo node = new NodeInfo(this.current_ddl.getTitle(), "ddl", c_id);
    DefaultMutableTreeNode nDdl = new DefaultMutableTreeNode(node);
    DefaultTreeModel NaviModel = (DefaultTreeModel)this.NaviTree.getModel();

    if (this.popnode.getType().equals("ddl"))
    {
      NaviModel.insertNodeInto(nDdl, (MutableTreeNode)this.datanode.getParent(), 0);
    }
    else if (this.popnode.getType().equals("user"))
    {
      NaviModel.insertNodeInto(nDdl, this.datanode, 0);
    }

    this.new_ddl_id += 1;

    return true;
  }

  private void commitDDL() throws TransformerConfigurationException, TransformerException, RemoteException
  {
    if (this.current_ddl == null)
    {
      return;
    }

    if (checkAttributeTable())
    {
      return;
    }

    if (this.FieldTypeVariable.isSelected())
    {
      if (!checkVariableValidity());
    }
    else if (checkFixedValidity())
    {
      return;
    }

    int id = this.current_ddl.getId();

    this.ol.setComments(id, this.DDLDefComment.getText());

    if (this.FieldTypeVariable.isSelected())
    {
      this.ol.setFieldFormatType(id, true);
      this.ol.setVariableFieldDelimeter(id, this.VFFieldDelimeter.getText());
      this.ol.setVariableRecordDelimeter(id, this.VFRecordDelimeter.getText());
      this.ol.setVariableItemsetUse(id, this.UseItemsetCheck.isSelected());

      if (this.UseItemsetCheck.isSelected())
      {
        if (this.ItemsetDifferent.isSelected())
        {
          this.ol.setVariableItemsetDelimeter(id, this.ItemsetDelimeter.getText());
        }
        else
        {
          this.ol.setVariableItemsetDelimeter(id, this.VFFieldDelimeter.getText());
        }

        this.ol.setVariableItemsetTidField(id, this.current_ddl.getItemsetTidField());
        this.ol.setVariableItemsetField(id, this.current_ddl.getItemsetField());
      }

    }
    else
    {
      this.ol.setFieldFormatType(id, false);
      Vector fixedP = this.current_ddl.getFixedField();

      for (int i = 0; i < fixedP.size(); i++)
      {
        this.ol.setFixedPosition(id, ((FixedPosition)fixedP.get(i)).getId(), (FixedPosition)fixedP.get(i));
      }
    }

    Vector atrs = this.current_ddl.getAttribute();

    for (int i = 0; i < atrs.size(); i++)
    {
      this.ol.setAttribute(id, ((Attribute)atrs.get(i)).getId(), (Attribute)atrs.get(i));
    }

    for (int i = atrs.size(); i < this.old_attributes; i++)
    {
      this.ol.removeAttribute(id, i + 1);
    }

    this.ol.rebuildXmlFile(this.out_filename);
    this.current_ddl = this.ol.getDataDefintion(this.current_ddl.getId());
    initDDLDetailTab1(this.current_ddl);
    initDDLDetailTab2(this.current_ddl);
    initDDLDetailTab3(this.current_ddl);
  }

  private void initComponents()
  {
    this.DDLStrFixedPanel = new JPanel();
    this.jScrollPane7 = new JScrollPane();
    this.DDLStrFixedTable = new JTable();
    this.DDLList = new JPanel();
    this.DDLListScroll = new JScrollPane();
    this.DDLListTable = new JTable();
    this.DBMainList = new JPanel();
    this.DBMainListScroll = new JScrollPane();
    this.DBMainListTable = new JTable();
    this.DDLDetail = new JPanel();
    this.DDLDetailTab = new JTabbedPane();
    this.DDLStructure = new JPanel();
    this.jLabel3 = new JLabel();
    this.DDLStrName = new JLabel();
    this.jLabel5 = new JLabel();
    this.jScrollPane1 = new JScrollPane();
    this.DDLStrComment = new JTextArea();
    this.jLabel6 = new JLabel();
    this.jScrollPane2 = new JScrollPane();
    this.DDLStrTable = new JTable();
    this.DDLStrType = new JLabel();
    this.DDLStrSetting = new JPanel();
    this.DDLDefinition = new JPanel();
    this.jPanel4 = new JPanel();
    this.jLabel12 = new JLabel();
    this.DDLDefName = new JLabel();
    this.jLabel14 = new JLabel();
    this.DDLDefDate = new JLabel();
    this.jPanel5 = new JPanel();
    this.jScrollPane4 = new JScrollPane();
    this.DDLDefComment = new JTextArea();
    this.jPanel6 = new JPanel();
    this.jLabel16 = new JLabel();
    this.jScrollPane5 = new JScrollPane();
    this.DDLDefTable = new JTable();
    this.FieldTypeVariable = new JRadioButton();
    this.FieldTypeFixed = new JRadioButton();
    this.indetailButton = new JButton();
    this.DetailToolBar = new JToolBar();
    this.AddField = new JButton();
    this.RemoveField = new JButton();
    this.CommitField = new JButton();
    this.DDLData = new JPanel();
    this.jLabel11 = new JLabel();
    this.jTextField1 = new JTextField();
    this.jButton1 = new JButton();
    this.jScrollPane3 = new JScrollPane();
    this.DDLDataTable = new JTable();
    this.jLabel9 = new JLabel();
    this.jTextField2 = new JTextField();
    this.jButton10 = new JButton();
    this.DelimeterMode = new ButtonGroup();
    this.FixedFrame = new JFrame();
    this.TypeComboBox = new JComboBox();
    this.PopupMenu1 = new JPopupMenu();
    this.jMenuItem1 = new JMenuItem();
    this.jMenuItem2 = new JMenuItem();
    this.copyDDLMenuItem = new JMenuItem();
    this.panel1 = new Panel();
    this.UserList = new JPanel();
    this.UserListScroll = new JScrollPane();
    this.UserListTable = new JTable();
    this.ItemsetSelectGroup = new ButtonGroup();
    this.DDLStrVariablePanel = new JPanel();
    this.jLabel7 = new JLabel();
    this.jLabel8 = new JLabel();
    this.DDLStrRecordDel = new JLabel();
    this.DDLStrFieldDel = new JLabel();
    this.VariableDialog = new JDialog();
    this.jLabel17 = new JLabel();
    this.jLabel18 = new JLabel();
    this.VFFieldDelimeter = new JTextField();
    this.VFRecordDelimeter = new JTextField();
    this.jButton3 = new JButton();
    this.jButton4 = new JButton();
    this.UseItemsetCheck = new JCheckBox();
    this.ItemsetDifferent = new JRadioButton();
    this.ItemsetDelimeter = new JTextField();
    this.ItemsetSame = new JRadioButton();
    this.ItemsetTableScroll = new JScrollPane();
    this.ItemsetTable = new JTable();
    this.FixedDialog = new JDialog();
    this.jScrollPane6 = new JScrollPane();
    this.FixedTable = new JTable();
    this.jButton5 = new JButton();
    this.jButton6 = new JButton();
    this.FieldAddDialog = new JDialog();
    this.FieldAddFrameName = new JLabel();
    this.jButton7 = new JButton();
    this.jButton8 = new JButton();
    this.jLabel20 = new JLabel();
    this.FieldAddName = new JTextField();
    this.jLabel21 = new JLabel();
    this.FieldAddType = new JComboBox();
    this.FieldAddNull = new JCheckBox();
    this.jLabel22 = new JLabel();
    this.FieldAddDefault = new JTextField();
    this.DDLCreateDialog = new JDialog();
    this.jPanel9 = new JPanel();
    this.jScrollPane8 = new JScrollPane();
    this.DDLCreateComment = new JTextArea();
    this.jPanel10 = new JPanel();
    this.jLabel19 = new JLabel();
    this.jScrollPane9 = new JScrollPane();
    this.DDLCreateTable = new JTable();
    this.CreateFieldTypeVariable = new JRadioButton();
    this.CreateFieldTypeFixed = new JRadioButton();
    this.createIndetailButton = new JButton();
    this.DetailToolBar1 = new JToolBar();
    this.CreateAddField = new JButton();
    this.CreateRemoveField = new JButton();
    this.jLabel1 = new JLabel();
    this.DDLCreateName = new JTextField();
    this.CreateOK = new JButton();
    this.CreateCancle = new JButton();
    this.jLabel4 = new JLabel();
    this.CreateOwnerName = new JLabel();

    this.TaskUserList = new JPanel();

    this.TuskUserListScroll = new JScrollPane();

    this.TaskUserListTable = new JTable();

    this.TaskList = new JPanel();

    this.TaskListScroll = new JScrollPane();

    this.TaskListTable = new JTable();
    this.userPopUp = new JPopupMenu();
    this.item_insertTask = new JMenuItem();
    this.taskPopUp = new JPopupMenu();
    this.item_insertJob = new JMenuItem();
    this.item_runTask = new JMenuItem();
    this.item_stopTask = new JMenuItem();
    this.item_preloadTask = new JMenuItem();
    this.item_taskProperty = new JMenuItem();
    this.item_removeTask = new JMenuItem();
    this.item_copyTask = new JMenuItem();
    this.item_makeTemplate = new JMenuItem();
    this.item_hidden = new JMenuItem();
    this.jobPopUp = new JPopupMenu();
    this.item_removeJob = new JMenuItem();
    this.item_jobProperty = new JMenuItem();
    this.item_parameter = new JMenuItem();
    this.item_display = new JMenuItem();
    this.refreshPopup = new JPopupMenu();
    this.refreshMenu = new JMenuItem();
    this.PipeSelectDlg = new JDialog();
    this.jScrollPane10 = new JScrollPane();
    this.PipeSelectTable = new JTable();
    this.jLabel10 = new JLabel();
    this.jComboBox1 = new JComboBox();
    this.jButton11 = new JButton();
    this.jButton12 = new JButton();
    this.PopupMenu2 = new JPopupMenu();
    this.jMenuItem3 = new JMenuItem();
    this.taskPopUpForMS = new JPopupMenu();
    this.item_insertQuery2 = new JMenuItem();
    this.item_planGeneration = new JMenuItem();
    this.item_runTask1 = new JMenuItem();
    this.item_stopTask1 = new JMenuItem();
    this.item_taskProperty1 = new JMenuItem();
    this.item_removeTask1 = new JMenuItem();
    this.ipTablePopUp = new JPopupMenu();
    this.item_insert_IP_Table = new JMenuItem();
    this.ipTableTaskInfoPanel = new JPanel();
    this.OnwerLabel = new JLabel();
    this.jLabel13 = new JLabel();
    this.nameTextField = new JTextField();
    this.informationPanel = new JPanel();
    this.ipAddressTextField = new JTextField();
    this.statusTextField = new JTextField();
    this.heartbeatPortTextField = new JTextField();
    this.jLabel25 = new JLabel();
    this.jLabel27 = new JLabel();
    this.jLabel28 = new JLabel();
    this.jLabel29 = new JLabel();
    this.jLabel31 = new JLabel();
    this.rmiSuffixTextField = new JTextField();
    this.jLabel32 = new JLabel();
    this.inputPortTextField = new JTextField();
    this.WorkerComboBox = new JComboBox();
    this.jLabel24 = new JLabel();
    this.jLabel33 = new JLabel();
    this.jLabel34 = new JLabel();
    this.jLabel35 = new JLabel();
    this.jLabel36 = new JLabel();
    this.jLabel37 = new JLabel();
    this.jLabel38 = new JLabel();
    this.jLabel39 = new JLabel();
    this.jLabel40 = new JLabel();
    this.jLabel41 = new JLabel();
    this.jLabel42 = new JLabel();
    this.jLabel43 = new JLabel();
    this.applyButton = new JButton();
    this.removeIPTableButton = new JButton();
    this.ipTableTaskListPanel = new JPanel();
    this.jScrollPane12 = new JScrollPane();
    this.ipTableTaskListTable = new JTable();
    this.jButton13 = new JButton();
    this.jButton14 = new JButton();
    this.jLabel26 = new JLabel();
    this.jLabel30 = new JLabel();
    this.TimerPeriodTextField = new JTextField();
    this.jButton15 = new JButton();
    this.IP_TableRefreshButton = new JButton();
    this.jLabel23 = new JLabel();
    this.jLabel15 = new JLabel();
    this.systemMonitorDialog = new JDialog();
    this.jPanel2 = new JPanel();
    this.memoryUsageLabel = new JLabel();
    this.cpuUsageLabel = new JLabel();
    this.systemTimeLabel = new JLabel();
    this.jLabel46 = new JLabel();
    this.jLabel47 = new JLabel();
    this.endTimeLabel = new JLabel();
    this.jLabel48 = new JLabel();
    this.jLabel49 = new JLabel();
    this.jLabel50 = new JLabel();
    this.jLabel51 = new JLabel();
    this.inputRateLabel = new JLabel();
    this.jLabel52 = new JLabel();
    this.taskNameTextField = new JTextField();
    this.startTimeTextField = new JTextField();
    this.jLabel55 = new JLabel();
    this.elapsedTimeLabel = new JLabel();
    this.jLabel57 = new JLabel();
    this.queueInfoLabel = new JLabel();
    this.jLabel58 = new JLabel();
    this.executionStateLabel = new JLabel();
    this.jPanel7 = new JPanel();
    this.autoTerminateChangeTextField = new JTextField();
    this.autoTerminateRepeatTextField = new JTextField();
    this.autoTerminateOutputCountLabel = new JLabel();
    this.autoTerminateTupleRepeatLabel = new JLabel();
    this.exportButton = new JButton();
    this.jLabel53 = new JLabel();
    this.jLabel54 = new JLabel();
    this.exportLocationTextField = new JTextField();
    this.viewSystemMonitorButton = new JButton();
    this.mapReduceSIMComboBox = new JComboBox();
    this.jLabel56 = new JLabel();
    this.systemMonitorViewDialog = new JDialog();
    this.MainFrame = new JSplitPane();
    this.Navis = new JSplitPane();
    this.NaviFrame = new JSplitPane();
    this.jPanel1 = new JPanel();
    this.NaviLabel = new JLabel();
    this.NaviScroll = new JScrollPane();
    this.NaviTree = new JTree();
    this.TasksFrame = new JSplitPane();
    this.jPanel3 = new JPanel();
    this.jLabel2 = new JLabel();
    this.TasksScroll = new JScrollPane();
    this.TasksTable1 = new JTable();
    this.jScrollPane11 = new JScrollPane();
    this.Controllers = new JPanel();
    this.jToolBar1 = new JToolBar();
    this.jButton2 = new JButton();
    this.jButton9 = new JButton();
    this.jSeparator2 = new JToolBar.Separator();
    this.jLabel45 = new JLabel();
    this.TaskRunTBtn = new JButton();
    this.TaskStopTBtn = new JButton();
    this.TaskRerunTBtn = new JButton();
    this.TaskPauseTBtn = new JButton();
    this.jSeparator3 = new JToolBar.Separator();
    this.jLabel44 = new JLabel();
    this.MapReduceRunBtn = new JButton();
    this.MapReduceStopBtn = new JButton();
    this.jSeparator4 = new JToolBar.Separator();
    this.systemMonitorButton = new JButton();
    this.MenuBar = new JMenuBar();
    this.jMenu1 = new JMenu();
    this.jMenu2 = new JMenu();
    this.jMenu3 = new JMenu();
    this.jMenu4 = new JMenu();
    this.jMenu5 = new JMenu();
    this.jMenu6 = new JMenu();

    this.DDLStrFixedPanel.setBorder(BorderFactory.createTitledBorder(null, "Setting", 0, 0, new Font("굴림", 0, 12), new Color(102, 102, 102)));
    this.DDLStrFixedPanel.setPreferredSize(new Dimension(524, 117));

    this.jScrollPane7.setAutoscrolls(true);
    this.jScrollPane7.setEnabled(false);

    this.DDLStrFixedTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "No", "Name", "Type", "Offset", "Length" })
    {
      Class[] types = { Integer.class, String.class, String.class, Integer.class, Integer.class };

      boolean[] canEdit = { false, false, false, false, false };

      public Class getColumnClass(int columnIndex)
      {
        return this.types[columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.canEdit[columnIndex];
      }
    });
    this.DDLStrFixedTable.setEnabled(false);
    this.DDLStrFixedTable.setFillsViewportHeight(true);
    this.jScrollPane7.setViewportView(this.DDLStrFixedTable);

    GroupLayout DDLStrFixedPanelLayout = new GroupLayout(this.DDLStrFixedPanel);
    this.DDLStrFixedPanel.setLayout(DDLStrFixedPanelLayout);
    DDLStrFixedPanelLayout.setHorizontalGroup(DDLStrFixedPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jScrollPane7, -1, 512, 32767));

    DDLStrFixedPanelLayout.setVerticalGroup(DDLStrFixedPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jScrollPane7, -1, 91, 32767));

    this.DDLList.setLayout(new BorderLayout());

    this.DDLListScroll.setAutoscrolls(true);

    this.DDLListTable.setAutoCreateRowSorter(true);
    this.DDLListTable.setModel(new DefaultTableModel(new Object[][] { { "User", "DDL", "This is a DDL sample" } }, new String[] { "Title", "Type", "Comment" })
    {
      boolean[] canEdit = { false, false, false };

      public boolean isCellEditable(int rowIndex, int columnIndex)
      {
        return this.canEdit[columnIndex];
      }
    });
    this.DDLListTable.setFillsViewportHeight(true);
    this.DDLListTable.setGridColor(new Color(255, 255, 255));
    this.DDLListTable.getTableHeader().setReorderingAllowed(false);
    this.DDLListScroll.setViewportView(this.DDLListTable);

    this.DDLList.add(this.DDLListScroll, "Center");

    this.DBMainList.setLayout(new BorderLayout());

    this.DBMainListScroll.setAutoscrolls(true);

    this.DBMainListTable.setAutoCreateRowSorter(true);
    this.DBMainListTable.setModel(new DefaultTableModel(new Object[][] { { "Data Definition", "Folder" }, { "Tasks", "TASKS" } }, new String[] { "Name", "Type" })
    {
      boolean[] canEdit = { false, false };

      public boolean isCellEditable(int rowIndex, int columnIndex)
      {
        return this.canEdit[columnIndex];
      }
    });
    this.DBMainListTable.setFillsViewportHeight(true);
    this.DBMainListTable.setGridColor(new Color(255, 255, 255));
    this.DBMainListTable.getTableHeader().setReorderingAllowed(false);
    this.DBMainListScroll.setViewportView(this.DBMainListTable);

    this.DBMainList.add(this.DBMainListScroll, "Center");

    this.DDLDetail.setLayout(new BorderLayout());

    this.DDLDetailTab.setTabLayoutPolicy(1);

    this.jLabel3.setText("Title :");

    this.jLabel5.setText("Comments");

    this.jScrollPane1.setEnabled(false);

    this.DDLStrComment.setColumns(20);
    this.DDLStrComment.setRows(5);
    this.DDLStrComment.setEnabled(false);
    this.jScrollPane1.setViewportView(this.DDLStrComment);

    this.jLabel6.setText("Field Construction Type : ");

    this.DDLStrTable.setAutoCreateRowSorter(true);
    this.DDLStrTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "No", "Name", "Type", "Size", "Allow NULLS", "Default" })
    {
      Class[] types = { Integer.class, String.class, String.class, Integer.class, Boolean.class, String.class };

      boolean[] canEdit = { false, false, false, false, false, false };

      public Class getColumnClass(int columnIndex)
      {
        return this.types[columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.canEdit[columnIndex];
      }
    });
    this.DDLStrTable.setFillsViewportHeight(true);
    this.DDLStrTable.setGridColor(new Color(255, 255, 255));
    this.DDLStrTable.getTableHeader().setReorderingAllowed(false);
    this.jScrollPane2.setViewportView(this.DDLStrTable);
    if (this.DDLStrTable.getColumnModel().getColumnCount() > 0) {
      this.DDLStrTable.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.DDLStrTable.getColumnModel().getColumn(1).setPreferredWidth(150);
      this.DDLStrTable.getColumnModel().getColumn(2).setPreferredWidth(20);
      this.DDLStrTable.getColumnModel().getColumn(3).setPreferredWidth(6);
      this.DDLStrTable.getColumnModel().getColumn(4).setPreferredWidth(50);
      this.DDLStrTable.getColumnModel().getColumn(5).setPreferredWidth(30);
    }

    this.DDLStrSetting.setPreferredSize(new Dimension(524, 117));
    this.DDLStrSetting.setLayout(new BorderLayout());

    GroupLayout DDLStructureLayout = new GroupLayout(this.DDLStructure);
    this.DDLStructure.setLayout(DDLStructureLayout);
    DDLStructureLayout.setHorizontalGroup(DDLStructureLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(DDLStructureLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(DDLStructureLayout
      .createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addComponent(this.jScrollPane2, GroupLayout.Alignment.LEADING, -1, 559, 32767)
      .addGroup(GroupLayout.Alignment.LEADING, DDLStructureLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING, false)
      .addGroup(DDLStructureLayout
      .createSequentialGroup()
      .addComponent(this.jLabel3)
      .addGap(18, 18, 18)
      .addComponent(this.DDLStrName))
      .addComponent(this.jLabel5)
      .addGroup(DDLStructureLayout
      .createSequentialGroup()
      .addComponent(this.jLabel6)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.DDLStrType))
      .addComponent(this.DDLStrSetting, -1, -1, 32767)
      .addComponent(this.jScrollPane1, -1, 559, 32767)))
      .addContainerGap(17, 32767)));

    DDLStructureLayout.setVerticalGroup(DDLStructureLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(DDLStructureLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(DDLStructureLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel3)
      .addComponent(this.DDLStrName))
      .addGap(18, 18, 18)
      .addComponent(this.jLabel5)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jScrollPane1, -2, 64, -2)
      .addGap(18, 18, 18)
      .addGroup(DDLStructureLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel6)
      .addComponent(this.DDLStrType))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.DDLStrSetting, -2, 117, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
      .addComponent(this.jScrollPane2, -2, 150, -2)
      .addContainerGap()));

    this.DDLDetailTab.addTab("Structure", this.DDLStructure);

    this.jPanel4.setBorder(BorderFactory.createTitledBorder(null, "Identification", 0, 0, new Font("굴림", 0, 12), new Color(102, 102, 102)));

    this.jLabel12.setText("Name :");

    this.jLabel14.setText("Creation Date :");

    GroupLayout jPanel4Layout = new GroupLayout(this.jPanel4);
    this.jPanel4.setLayout(jPanel4Layout);
    jPanel4Layout.setHorizontalGroup(jPanel4Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout
      .createSequentialGroup()
      .addContainerGap()
      .addComponent(this.jLabel12)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.DDLDefName, -2, 134, -2)
      .addGap(33, 33, 33)
      .addComponent(this.jLabel14)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.DDLDefDate, -2, 134, -2)
      .addContainerGap(87, 32767)));

    jPanel4Layout.setVerticalGroup(jPanel4Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel4Layout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(jPanel4Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.DDLDefName, -1, -1, 32767)
      .addGroup(jPanel4Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel12)
      .addComponent(this.jLabel14)
      .addComponent(this.DDLDefDate, -2, 20, -2)))
      .addContainerGap()));

    this.jPanel5.setBorder(BorderFactory.createTitledBorder(null, "Comments", 0, 0, new Font("굴림", 0, 12), new Color(102, 102, 102)));

    this.DDLDefComment.setColumns(20);
    this.DDLDefComment.setRows(5);
    this.jScrollPane4.setViewportView(this.DDLDefComment);

    GroupLayout jPanel5Layout = new GroupLayout(this.jPanel5);
    this.jPanel5.setLayout(jPanel5Layout);
    jPanel5Layout.setHorizontalGroup(jPanel5Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout
      .createSequentialGroup()
      .addContainerGap()
      .addComponent(this.jScrollPane4, -1, 521, 32767)
      .addContainerGap()));

    jPanel5Layout.setVerticalGroup(jPanel5Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel5Layout
      .createSequentialGroup()
      .addComponent(this.jScrollPane4, -2, 62, -2)
      .addContainerGap(-1, 32767)));

    this.jPanel6.setBorder(BorderFactory.createTitledBorder(null, "Attributes", 0, 0, new Font("굴림", 0, 12), new Color(102, 102, 102)));

    this.jLabel16.setText("Field Format Type :");

    this.DDLDefTable.setAutoCreateRowSorter(true);
    this.DDLDefTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "No", "Name", "Type", "Size", "Allow NULLS", "Default" })
    {
      Class[] types = { Integer.class, String.class, String.class, Integer.class, Boolean.class, String.class };

      boolean[] canEdit = { false, true, true, false, true, true };

      public Class getColumnClass(int columnIndex)
      {
        return this.types[columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.canEdit[columnIndex];
      }
    });
    this.DDLDefTable.setFillsViewportHeight(true);
    this.DDLDefTable.getTableHeader().setReorderingAllowed(false);
    this.DDLDefTable.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        iesMainFrm.this.DDLDefTablePropertyChange(evt);
      }
    });
    this.jScrollPane5.setViewportView(this.DDLDefTable);
    if (this.DDLDefTable.getColumnModel().getColumnCount() > 0) {
      this.DDLDefTable.getColumnModel().getColumn(0).setPreferredWidth(5);
      this.DDLDefTable.getColumnModel().getColumn(1).setPreferredWidth(150);
      this.DDLDefTable.getColumnModel().getColumn(2).setPreferredWidth(20);
      this.DDLDefTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(this.TypeComboBox));
      this.DDLDefTable.getColumnModel().getColumn(3).setPreferredWidth(6);
      this.DDLDefTable.getColumnModel().getColumn(4).setPreferredWidth(50);
      this.DDLDefTable.getColumnModel().getColumn(5).setPreferredWidth(30);
    }

    this.DelimeterMode.add(this.FieldTypeVariable);
    this.FieldTypeVariable.setText("Variable");
    this.FieldTypeVariable.setActionCommand("FieldTypeVariable");
    this.FieldTypeVariable.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.FieldTypeVariableActionPerformed(evt);
      }
    });
    this.DelimeterMode.add(this.FieldTypeFixed);
    this.FieldTypeFixed.setText("Fixed");
    this.FieldTypeFixed.setActionCommand("FieldTypeFixed");
    this.FieldTypeFixed.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.FieldTypeFixedActionPerformed(evt);
      }
    });
    this.indetailButton.setText("in Detail");
    this.indetailButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.indetailButtonActionPerformed(evt);
      }
    });
    this.DetailToolBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    this.DetailToolBar.setFloatable(false);
    this.DetailToolBar.setRollover(true);

    this.AddField.setIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.AddField.setToolTipText("Add Field");
    this.AddField.setDisabledIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.AddField.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.AddField.setFocusable(false);
    this.AddField.setHorizontalTextPosition(0);
    this.AddField.setPressedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.AddField.setRolloverIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.AddField.setRolloverSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.AddField.setSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.AddField.setVerticalTextPosition(3);
    this.AddField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.AddFieldActionPerformed(evt);
      }
    });
    this.DetailToolBar.add(this.AddField);

    this.RemoveField.setIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.RemoveField.setToolTipText("Remove Field");
    this.RemoveField.setDisabledIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.RemoveField.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.RemoveField.setFocusable(false);
    this.RemoveField.setHorizontalTextPosition(0);
    this.RemoveField.setPressedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.RemoveField.setRolloverIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.RemoveField.setRolloverSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.RemoveField.setSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.RemoveField.setVerticalTextPosition(3);
    this.RemoveField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.RemoveFieldActionPerformed(evt);
      }
    });
    this.DetailToolBar.add(this.RemoveField);

    this.CommitField.setIcon(new ImageIcon(getClass().getResource("/dbies/imgs/commit.gif")));
    this.CommitField.setToolTipText("Commit Changes");
    this.CommitField.setDisabledIcon(new ImageIcon(getClass().getResource("/dbies/imgs/commit.gif")));
    this.CommitField.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/commit.gif")));
    this.CommitField.setFocusable(false);
    this.CommitField.setHorizontalTextPosition(0);
    this.CommitField.setPressedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/commit.gif")));
    this.CommitField.setRolloverIcon(new ImageIcon(getClass().getResource("/dbies/imgs/commit.gif")));
    this.CommitField.setRolloverSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/commit.gif")));
    this.CommitField.setSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/commit.gif")));
    this.CommitField.setVerticalTextPosition(3);
    this.CommitField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.CommitFieldActionPerformed(evt);
      }
    });
    this.DetailToolBar.add(this.CommitField);

    GroupLayout jPanel6Layout = new GroupLayout(this.jPanel6);
    this.jPanel6.setLayout(jPanel6Layout);
    jPanel6Layout.setHorizontalGroup(jPanel6Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel6Layout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(jPanel6Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jScrollPane5, -1, 523, 32767)
      .addGroup(jPanel6Layout
      .createSequentialGroup()
      .addComponent(this.jLabel16)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.FieldTypeVariable)
      .addGap(18, 18, 18)
      .addComponent(this.FieldTypeFixed)
      .addGap(18, 18, 18)
      .addComponent(this.indetailButton)))
      .addContainerGap())
      .addGroup(jPanel6Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.TRAILING, jPanel6Layout
      .createSequentialGroup()
      .addContainerGap(435, 32767)
      .addComponent(this.DetailToolBar, -2, 98, -2)
      .addContainerGap())));

    jPanel6Layout.setVerticalGroup(jPanel6Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel6Layout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(jPanel6Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel16)
      .addComponent(this.FieldTypeVariable)
      .addComponent(this.FieldTypeFixed)
      .addComponent(this.indetailButton))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jScrollPane5, -1, 172, 32767)
      .addContainerGap())
      .addGroup(jPanel6Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel6Layout
      .createSequentialGroup()
      .addComponent(this.DetailToolBar, -2, 39, -2)
      .addContainerGap(182, 32767))));

    this.FieldTypeVariable.getAccessibleContext().setAccessibleName("FieldTypeVariable");
    this.FieldTypeFixed.getAccessibleContext().setAccessibleName("FieldTypeFixed");

    GroupLayout DDLDefinitionLayout = new GroupLayout(this.DDLDefinition);
    this.DDLDefinition.setLayout(DDLDefinitionLayout);
    DDLDefinitionLayout.setHorizontalGroup(DDLDefinitionLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(DDLDefinitionLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(DDLDefinitionLayout
      .createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addComponent(this.jPanel6, GroupLayout.Alignment.LEADING, -1, -1, 32767)
      .addGroup(GroupLayout.Alignment.LEADING, DDLDefinitionLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING, false)
      .addComponent(this.jPanel4, -1, -1, 32767)
      .addComponent(this.jPanel5, -1, -1, 32767)))
      .addContainerGap(17, 32767)));

    DDLDefinitionLayout.setVerticalGroup(DDLDefinitionLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(DDLDefinitionLayout
      .createSequentialGroup()
      .addGap(5, 5, 5)
      .addComponent(this.jPanel4, -2, -1, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jPanel5, -2, -1, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jPanel6, -2, -1, -2)
      .addGap(20, 20, 20)));

    this.DDLDetailTab.addTab("Definition", this.DDLDefinition);

    this.jLabel11.setText("Fetch Size :");

    this.jTextField1.setText("100");
    this.jTextField1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jTextField1ActionPerformed(evt);
      }
    });
    this.jButton1.setText("Fetch Next");
    this.jButton1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton1ActionPerformed(evt);
      }
    });
    this.DDLDataTable.setAutoCreateRowSorter(true);
    this.DDLDataTable.setModel(new DefaultTableModel(new Object[][] { { "John", "2", "1521", "john@hotmail.com" }, { "Tom", "2", "1522", "tom@yahoo.com" }, { "Harry", "3", "1417", "harry@google.com" } }, new String[] { "name", "department", "phone", "email" }));

    this.DDLDataTable.setFillsViewportHeight(true);
    this.DDLDataTable.setGridColor(new Color(204, 204, 204));
    this.DDLDataTable.getTableHeader().setReorderingAllowed(false);
    this.jScrollPane3.setViewportView(this.DDLDataTable);

    this.jLabel9.setText("Pipe Name : ");

    this.jTextField2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jTextField2ActionPerformed(evt);
      }
    });
    this.jButton10.setText("select");
    this.jButton10.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton10ActionPerformed(evt);
      }
    });
    GroupLayout DDLDataLayout = new GroupLayout(this.DDLData);
    this.DDLData.setLayout(DDLDataLayout);
    DDLDataLayout.setHorizontalGroup(DDLDataLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(DDLDataLayout
      .createSequentialGroup()
      .addGroup(DDLDataLayout
      .createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addGroup(DDLDataLayout
      .createSequentialGroup()
      .addContainerGap()
      .addComponent(this.jLabel11)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jTextField1, -2, 67, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jButton1)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
      .addComponent(this.jLabel9)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jTextField2, -2, 89, -2)
      .addGap(5, 5, 5)
      .addComponent(this.jButton10))
      .addComponent(this.jScrollPane3, GroupLayout.Alignment.LEADING, -2, 574, -2))
      .addContainerGap(14, 32767)));

    DDLDataLayout.setVerticalGroup(DDLDataLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(DDLDataLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(DDLDataLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel11)
      .addComponent(this.jTextField1, -2, -1, -2)
      .addComponent(this.jButton1)
      .addComponent(this.jButton10)
      .addComponent(this.jTextField2, -2, -1, -2)
      .addComponent(this.jLabel9))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jScrollPane3, -1, 406, 32767)));

    this.DDLDetailTab.addTab("Data", this.DDLData);

    this.DDLDetail.add(this.DDLDetailTab, "First");

    this.FixedFrame.setTitle("Fixed in detail");
    this.FixedFrame.setName("indetail_fixed");

    GroupLayout FixedFrameLayout = new GroupLayout(this.FixedFrame.getContentPane());
    this.FixedFrame.getContentPane().setLayout(FixedFrameLayout);
    FixedFrameLayout.setHorizontalGroup(FixedFrameLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGap(0, 0, 32767));

    FixedFrameLayout.setVerticalGroup(FixedFrameLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGap(0, 0, 32767));

    this.TypeComboBox.setModel(new DefaultComboBoxModel(new String[] { "string", "integer", "double", "long" }));

    this.PopupMenu1.setBorderPainted(false);
    this.PopupMenu1.setLightWeightPopupEnabled(false);

    this.jMenuItem1.setText("Create");
    this.jMenuItem1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jMenuItem1ActionPerformed(evt);
      }
    });
    this.PopupMenu1.add(this.jMenuItem1);

    this.jMenuItem2.setText("Remove");
    this.jMenuItem2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jMenuItem2ActionPerformed(evt);
      }
    });
    this.PopupMenu1.add(this.jMenuItem2);

    this.copyDDLMenuItem.setText("Copy DDL");
    this.copyDDLMenuItem.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.copyDDLMenuItemActionPerformed(evt);
      }
    });
    this.PopupMenu1.add(this.copyDDLMenuItem);

    GroupLayout panel1Layout = new GroupLayout(this.panel1);
    this.panel1.setLayout(panel1Layout);
    panel1Layout.setHorizontalGroup(panel1Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGap(0, 100, 32767));

    panel1Layout.setVerticalGroup(panel1Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGap(0, 100, 32767));

    this.UserList.setLayout(new BorderLayout());

    this.UserListScroll.setAutoscrolls(true);

    this.UserListTable.setAutoCreateRowSorter(true);
    this.UserListTable.setModel(new DefaultTableModel(new Object[][] { { null, null } }, new String[] { "Name", "Type" })
    {
      boolean[] canEdit = { false, false };

      public boolean isCellEditable(int rowIndex, int columnIndex)
      {
        return this.canEdit[columnIndex];
      }
    });
    this.UserListTable.setFillsViewportHeight(true);
    this.UserListTable.setGridColor(new Color(255, 255, 255));
    this.UserListTable.getTableHeader().setReorderingAllowed(false);
    this.UserListScroll.setViewportView(this.UserListTable);

    this.UserList.add(this.UserListScroll, "Center");

    this.DDLStrVariablePanel.setBorder(BorderFactory.createTitledBorder(null, "Setting", 0, 0, new Font("굴림", 0, 12), new Color(102, 102, 102)));

    this.jLabel7.setText("Field Delimeter :");

    this.jLabel8.setText("Record Delimeter :");

    GroupLayout DDLStrVariablePanelLayout = new GroupLayout(this.DDLStrVariablePanel);
    this.DDLStrVariablePanel.setLayout(DDLStrVariablePanelLayout);
    DDLStrVariablePanelLayout.setHorizontalGroup(DDLStrVariablePanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(DDLStrVariablePanelLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(DDLStrVariablePanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jLabel8)
      .addComponent(this.jLabel7))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(DDLStrVariablePanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.DDLStrFieldDel)
      .addComponent(this.DDLStrRecordDel))
      .addContainerGap(384, 32767)));

    DDLStrVariablePanelLayout.setVerticalGroup(DDLStrVariablePanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(DDLStrVariablePanelLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(DDLStrVariablePanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel7)
      .addComponent(this.DDLStrFieldDel))
      .addGap(18, 18, 18)
      .addGroup(DDLStrVariablePanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel8)
      .addComponent(this.DDLStrRecordDel))
      .addContainerGap(-1, 32767)));

    this.VariableDialog.setTitle("Configuration of Variable type");
    this.VariableDialog.setCursor(new Cursor(0));
    this.VariableDialog.setLocationByPlatform(true);
    this.VariableDialog.setModal(true);
    this.VariableDialog.setResizable(false);

    this.jLabel17.setText("Field Delimeter");

    this.jLabel18.setText("Record Delimeter");

    this.VFFieldDelimeter.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.VFFieldDelimeterActionPerformed(evt);
      }
    });
    this.VFRecordDelimeter.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.VFRecordDelimeterActionPerformed(evt);
      }
    });
    this.jButton3.setText("Confirm");
    this.jButton3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton3ActionPerformed(evt);
      }
    });
    this.jButton4.setText("Cancel");
    this.jButton4.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton4ActionPerformed(evt);
      }
    });
    this.UseItemsetCheck.setText("Use Itemset Data Field");
    this.UseItemsetCheck.setName("ItemsetUseCheck");
    this.UseItemsetCheck.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.UseItemsetCheckActionPerformed(evt);
      }
    });
    this.ItemsetSelectGroup.add(this.ItemsetDifferent);
    this.ItemsetDifferent.setSelected(true);
    this.ItemsetDifferent.setText("Use different delimeter");
    this.ItemsetDifferent.setEnabled(false);
    this.ItemsetDifferent.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.ItemsetDifferentActionPerformed(evt);
      }
    });
    this.ItemsetDelimeter.setEnabled(false);

    this.ItemsetSelectGroup.add(this.ItemsetSame);
    this.ItemsetSame.setText("Use same delimeter (you can use only 2 Fields in this case)");
    this.ItemsetSame.setEnabled(false);
    this.ItemsetSame.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.ItemsetSameActionPerformed(evt);
      }
    });
    this.ItemsetTableScroll.setAutoscrolls(true);
    this.ItemsetTableScroll.setEnabled(false);
    this.ItemsetTableScroll.setPreferredSize(new Dimension(460, 150));

    this.ItemsetTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "No", "Name", "TID", "Itemset" })
    {
      Class[] types = { Integer.class, String.class, Boolean.class, Boolean.class };

      boolean[] canEdit = { false, false, true, true };

      public Class getColumnClass(int columnIndex)
      {
        return this.types[columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.canEdit[columnIndex];
      }
    });
    this.ItemsetTable.setEnabled(false);
    this.ItemsetTable.setFillsViewportHeight(true);
    this.ItemsetTableScroll.setViewportView(this.ItemsetTable);

    GroupLayout VariableDialogLayout = new GroupLayout(this.VariableDialog.getContentPane());
    this.VariableDialog.getContentPane().setLayout(VariableDialogLayout);
    VariableDialogLayout.setHorizontalGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(VariableDialogLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addGroup(VariableDialogLayout
      .createSequentialGroup()
      .addComponent(this.jButton3)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jButton4))
      .addComponent(this.UseItemsetCheck, GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.LEADING, VariableDialogLayout
      .createSequentialGroup()
      .addGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addGroup(GroupLayout.Alignment.LEADING, VariableDialogLayout
      .createSequentialGroup()
      .addGap(21, 21, 21)
      .addGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.ItemsetSame)
      .addGroup(VariableDialogLayout
      .createSequentialGroup()
      .addComponent(this.ItemsetDifferent)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.ItemsetDelimeter))))
      .addGroup(GroupLayout.Alignment.LEADING, VariableDialogLayout
      .createSequentialGroup()
      .addGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jLabel18)
      .addComponent(this.jLabel17))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING, false)
      .addComponent(this.VFFieldDelimeter)
      .addComponent(this.VFRecordDelimeter, -1, 150, 32767))))
      .addGap(77, 77, 77))
      .addComponent(this.ItemsetTableScroll, GroupLayout.Alignment.LEADING, -2, 469, -2))
      .addContainerGap()));

    VariableDialogLayout.linkSize(0, new Component[] { this.jButton3, this.jButton4 });

    VariableDialogLayout.setVerticalGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(VariableDialogLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel17)
      .addComponent(this.VFFieldDelimeter, -2, -1, -2))
      .addGap(18, 18, 18)
      .addGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel18)
      .addComponent(this.VFRecordDelimeter, -2, -1, -2))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.UseItemsetCheck)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.ItemsetDifferent)
      .addComponent(this.ItemsetDelimeter, -2, -1, -2))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.ItemsetSame)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.ItemsetTableScroll, -2, -1, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(VariableDialogLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jButton4)
      .addComponent(this.jButton3))
      .addContainerGap(-1, 32767)));

    this.FixedDialog.setTitle("Configuration of Fixed type");
    this.FixedDialog.setLocationByPlatform(true);
    this.FixedDialog.setModal(true);
    this.FixedDialog.setResizable(false);

    this.FixedTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "No", "Name", "Type", "Offset", "Length" })
    {
      Class[] types = { Integer.class, String.class, String.class, Integer.class, Integer.class };

      boolean[] canEdit = { false, false, false, true, true };

      public Class getColumnClass(int columnIndex)
      {
        return this.types[columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.canEdit[columnIndex];
      }
    });
    this.FixedTable.setFillsViewportHeight(true);
    this.jScrollPane6.setViewportView(this.FixedTable);

    this.jButton5.setText("Cancel");
    this.jButton5.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton5ActionPerformed(evt);
      }
    });
    this.jButton6.setText("Confirm");
    this.jButton6.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton6ActionPerformed(evt);
      }
    });
    GroupLayout FixedDialogLayout = new GroupLayout(this.FixedDialog.getContentPane());
    this.FixedDialog.getContentPane().setLayout(FixedDialogLayout);
    FixedDialogLayout.setHorizontalGroup(FixedDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.TRAILING, FixedDialogLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(FixedDialogLayout
      .createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addComponent(this.jScrollPane6, GroupLayout.Alignment.LEADING, -1, 376, 32767)
      .addGroup(FixedDialogLayout
      .createSequentialGroup()
      .addComponent(this.jButton6)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jButton5)))
      .addContainerGap()));

    FixedDialogLayout.linkSize(0, new Component[] { this.jButton5, this.jButton6 });

    FixedDialogLayout.setVerticalGroup(FixedDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.TRAILING, FixedDialogLayout
      .createSequentialGroup()
      .addContainerGap()
      .addComponent(this.jScrollPane6, -1, 247, 32767)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(FixedDialogLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jButton5)
      .addComponent(this.jButton6))
      .addContainerGap()));

    this.FieldAddDialog.setTitle("Add new Attribute");
    this.FieldAddDialog.setLocationByPlatform(true);
    this.FieldAddDialog.setModal(true);
    this.FieldAddDialog.setResizable(false);

    this.jButton7.setText("Cancel");
    this.jButton7.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton7ActionPerformed(evt);
      }
    });
    this.jButton8.setText("Execute");
    this.jButton8.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton8ActionPerformed(evt);
      }
    });
    this.jLabel20.setText("Column Name");

    this.jLabel21.setText("Type");

    this.FieldAddType.setModel(new DefaultComboBoxModel(new String[] { "string", "integer", "double", "long" }));
    this.FieldAddType.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.FieldAddTypeActionPerformed(evt);
      }
    });
    this.FieldAddNull.setSelected(true);
    this.FieldAddNull.setText("Allow NULLS");
    this.FieldAddNull.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.FieldAddNullActionPerformed(evt);
      }
    });
    this.jLabel22.setText("Default Value");

    GroupLayout FieldAddDialogLayout = new GroupLayout(this.FieldAddDialog.getContentPane());
    this.FieldAddDialog.getContentPane().setLayout(FieldAddDialogLayout);
    FieldAddDialogLayout.setHorizontalGroup(FieldAddDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(FieldAddDialogLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(FieldAddDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.FieldAddNull)
      .addComponent(this.FieldAddName, -1, 380, 32767)
      .addGroup(FieldAddDialogLayout
      .createSequentialGroup()
      .addComponent(this.FieldAddFrameName)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 215, 32767)
      .addComponent(this.jButton8)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jButton7))
      .addComponent(this.jLabel20)
      .addComponent(this.jLabel21)
      .addComponent(this.FieldAddType, 0, 380, 32767)
      .addComponent(this.jLabel22)
      .addComponent(this.FieldAddDefault, -1, 380, 32767))
      .addContainerGap()));

    FieldAddDialogLayout.linkSize(0, new Component[] { this.jButton7, this.jButton8 });

    FieldAddDialogLayout.setVerticalGroup(FieldAddDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(FieldAddDialogLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(FieldAddDialogLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jButton7)
      .addComponent(this.jButton8)
      .addComponent(this.FieldAddFrameName))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.jLabel20)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.FieldAddName, -2, -1, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.jLabel21)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.FieldAddType, -2, -1, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.FieldAddNull)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.jLabel22)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.FieldAddDefault, -2, -1, -2)
      .addContainerGap(86, 32767)));

    this.DDLCreateDialog.setTitle("Create New Data Definition");
    this.DDLCreateDialog.setMinimumSize(new Dimension(600, 400));
    this.DDLCreateDialog.setModal(true);
    this.DDLCreateDialog.setResizable(false);
    this.DDLCreateDialog.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent evt) {
        iesMainFrm.this.DDLCreateDialogWindowClosed(evt);
      }
      public void windowClosing(WindowEvent evt) {
        iesMainFrm.this.DDLCreateDialogWindowClosing(evt);
      }
    });
    this.jPanel9.setBorder(BorderFactory.createTitledBorder(null, "Comments", 0, 0, new Font("굴림", 0, 12), new Color(102, 102, 102)));

    this.DDLCreateComment.setColumns(20);
    this.DDLCreateComment.setRows(5);
    this.jScrollPane8.setViewportView(this.DDLCreateComment);

    GroupLayout jPanel9Layout = new GroupLayout(this.jPanel9);
    this.jPanel9.setLayout(jPanel9Layout);
    jPanel9Layout.setHorizontalGroup(jPanel9Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel9Layout
      .createSequentialGroup()
      .addContainerGap()
      .addComponent(this.jScrollPane8, -1, 465, 32767)
      .addContainerGap()));

    jPanel9Layout.setVerticalGroup(jPanel9Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel9Layout
      .createSequentialGroup()
      .addComponent(this.jScrollPane8, -2, 62, -2)
      .addContainerGap(-1, 32767)));

    this.jPanel10.setBorder(BorderFactory.createTitledBorder(null, "Attributes", 0, 0, new Font("굴림", 0, 12), new Color(102, 102, 102)));

    this.jLabel19.setText("Field Format Type :");

    this.DDLCreateTable.setAutoCreateRowSorter(true);
    this.DDLCreateTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "No", "Name", "Type", "Size", "Allow NULLS", "Default" })
    {
      Class[] types = { Integer.class, String.class, String.class, Integer.class, Boolean.class, String.class };

      boolean[] canEdit = { false, true, true, false, true, true };

      public Class getColumnClass(int columnIndex)
      {
        return this.types[columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.canEdit[columnIndex];
      }
    });
    this.DDLCreateTable.setFillsViewportHeight(true);
    this.DDLCreateTable.getTableHeader().setReorderingAllowed(false);
    this.DDLCreateTable.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        iesMainFrm.this.DDLCreateTablePropertyChange(evt);
      }
    });
    this.jScrollPane9.setViewportView(this.DDLCreateTable);
    if (this.DDLCreateTable.getColumnModel().getColumnCount() > 0) {
      this.DDLCreateTable.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(this.TypeComboBox));
    }

    this.DelimeterMode.add(this.CreateFieldTypeVariable);
    this.CreateFieldTypeVariable.setSelected(true);
    this.CreateFieldTypeVariable.setText("Variable");
    this.CreateFieldTypeVariable.setActionCommand("FieldTypeVariable");
    this.CreateFieldTypeVariable.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.CreateFieldTypeVariableActionPerformed(evt);
      }
    });
    this.DelimeterMode.add(this.CreateFieldTypeFixed);
    this.CreateFieldTypeFixed.setText("Fixed");
    this.CreateFieldTypeFixed.setActionCommand("FieldTypeFixed");
    this.CreateFieldTypeFixed.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.CreateFieldTypeFixedActionPerformed(evt);
      }
    });
    this.createIndetailButton.setText("in Detail");
    this.createIndetailButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.createIndetailButtonActionPerformed(evt);
      }
    });
    this.DetailToolBar1.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    this.DetailToolBar1.setFloatable(false);
    this.DetailToolBar1.setRollover(true);

    this.CreateAddField.setIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.CreateAddField.setToolTipText("Add Field");
    this.CreateAddField.setBorderPainted(false);
    this.CreateAddField.setDisabledIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.CreateAddField.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.CreateAddField.setFocusable(false);
    this.CreateAddField.setHorizontalTextPosition(0);
    this.CreateAddField.setPressedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.CreateAddField.setRolloverIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.CreateAddField.setRolloverSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.CreateAddField.setSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/add.gif")));
    this.CreateAddField.setVerticalTextPosition(3);
    this.CreateAddField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.CreateAddFieldActionPerformed(evt);
      }
    });
    this.DetailToolBar1.add(this.CreateAddField);

    this.CreateRemoveField.setIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.CreateRemoveField.setToolTipText("Remove Field");
    this.CreateRemoveField.setBorderPainted(false);
    this.CreateRemoveField.setDisabledIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.CreateRemoveField.setDisabledSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.CreateRemoveField.setFocusable(false);
    this.CreateRemoveField.setHorizontalTextPosition(0);
    this.CreateRemoveField.setPressedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.CreateRemoveField.setRolloverIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.CreateRemoveField.setRolloverSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.CreateRemoveField.setSelectedIcon(new ImageIcon(getClass().getResource("/dbies/imgs/remove.gif")));
    this.CreateRemoveField.setVerticalTextPosition(3);
    this.CreateRemoveField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.CreateRemoveFieldActionPerformed(evt);
      }
    });
    this.DetailToolBar1.add(this.CreateRemoveField);

    GroupLayout jPanel10Layout = new GroupLayout(this.jPanel10);
    this.jPanel10.setLayout(jPanel10Layout);
    jPanel10Layout.setHorizontalGroup(jPanel10Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel10Layout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(jPanel10Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jScrollPane9, -1, 465, 32767)
      .addGroup(jPanel10Layout
      .createSequentialGroup()
      .addComponent(this.jLabel19)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.CreateFieldTypeVariable)
      .addGap(18, 18, 18)
      .addComponent(this.CreateFieldTypeFixed)
      .addGap(18, 18, 18)
      .addComponent(this.createIndetailButton)))
      .addContainerGap())
      .addGroup(jPanel10Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.TRAILING, jPanel10Layout
      .createSequentialGroup()
      .addContainerGap(379, 32767)
      .addComponent(this.DetailToolBar1, -2, 98, -2)
      .addContainerGap())));

    jPanel10Layout.setVerticalGroup(jPanel10Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel10Layout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(jPanel10Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel19)
      .addComponent(this.CreateFieldTypeVariable)
      .addComponent(this.CreateFieldTypeFixed)
      .addComponent(this.createIndetailButton))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jScrollPane9, -1, 121, 32767)
      .addContainerGap())
      .addGroup(jPanel10Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel10Layout
      .createSequentialGroup()
      .addComponent(this.DetailToolBar1, -2, 39, -2)
      .addContainerGap(131, 32767))));

    this.jLabel1.setText("Name :");

    this.CreateOK.setText("OK");
    this.CreateOK.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.CreateOKActionPerformed(evt);
      }
    });
    this.CreateCancle.setText("Cancel");
    this.CreateCancle.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.CreateCancleActionPerformed(evt);
      }
    });
    this.jLabel4.setText("Owner : ");

    GroupLayout DDLCreateDialogLayout = new GroupLayout(this.DDLCreateDialog.getContentPane());
    this.DDLCreateDialog.getContentPane().setLayout(DDLCreateDialogLayout);
    DDLCreateDialogLayout.setHorizontalGroup(DDLCreateDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(DDLCreateDialogLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(DDLCreateDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jPanel10, -1, -1, 32767)
      .addGroup(DDLCreateDialogLayout
      .createSequentialGroup()
      .addComponent(this.jLabel1)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.DDLCreateName, -2, 167, -2)
      .addGap(86, 86, 86)
      .addComponent(this.jLabel4)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.CreateOwnerName, -2, 121, -2))
      .addComponent(this.jPanel9, -1, -1, 32767)
      .addGroup(GroupLayout.Alignment.TRAILING, DDLCreateDialogLayout
      .createSequentialGroup()
      .addComponent(this.CreateOK)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.CreateCancle)))
      .addContainerGap()));

    DDLCreateDialogLayout.linkSize(0, new Component[] { this.CreateCancle, this.CreateOK });

    DDLCreateDialogLayout.setVerticalGroup(DDLCreateDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(DDLCreateDialogLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(DDLCreateDialogLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel1)
      .addComponent(this.DDLCreateName, -2, -1, -2)
      .addComponent(this.jLabel4)
      .addComponent(this.CreateOwnerName, -2, 21, -2))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jPanel9, -2, -1, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jPanel10, -2, -1, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(DDLCreateDialogLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.CreateOK)
      .addComponent(this.CreateCancle))
      .addContainerGap(63, 32767)));

    this.TaskUserList.setPreferredSize(new Dimension(452, 402));
    this.TaskUserList.setLayout(new BorderLayout());

    this.TuskUserListScroll.setAutoscrolls(true);

    this.TaskUserListTable.setAutoCreateRowSorter(true);
    this.TaskUserListTable.setModel(new DefaultTableModel(new Object[][] { { "Views", "TASKS" }, { "Tables", "TASKS" } }, new String[] { "Name", "Type" })
    {
      boolean[] canEdit = { false, false };

      public boolean isCellEditable(int rowIndex, int columnIndex)
      {
        return this.canEdit[columnIndex];
      }
    });
    this.TaskUserListTable.setFillsViewportHeight(true);
    this.TaskUserListTable.setGridColor(new Color(0, 0, 0));
    this.TaskUserListTable.setShowHorizontalLines(false);
    this.TaskUserListTable.setShowVerticalLines(false);
    this.TaskUserListTable.getTableHeader().setReorderingAllowed(false);
    this.TuskUserListScroll.setViewportView(this.TaskUserListTable);

    this.TaskUserList.add(this.TuskUserListScroll, "Center");

    this.TaskList.setPreferredSize(new Dimension(452, 402));
    this.TaskList.setLayout(new BorderLayout());

    this.TaskListScroll.setAutoscrolls(true);

    this.TaskListTable.setAutoCreateRowSorter(true);
    this.TaskListTable.setModel(new DefaultTableModel(new Object[][] { { "NSH", "FOLDER" }, { "CTXSYS", "FOLDER" }, { "DMSYS", "FOLDER" }, { "EXFSYS", "FOLDER" }, { "MDSYS", "FOLDER" }, { "OLAPSYS", "FOLDER" }, { "ORDSYS", "FOLDER" }, { "OUTLN", "FOLDER" }, { "SYS", "FOLDER" }, { "SYSTEM", "FOLDER" }, { "WMSYS", "FOLDER" }, { "XDB", null } }, new String[] { "Name", "Type" })
    {
      boolean[] canEdit = { false, false };

      public boolean isCellEditable(int rowIndex, int columnIndex)
      {
        return this.canEdit[columnIndex];
      }
    });
    this.TaskListTable.setFillsViewportHeight(true);
    this.TaskListTable.setGridColor(new Color(0, 0, 0));
    this.TaskListTable.setShowHorizontalLines(false);
    this.TaskListTable.setShowVerticalLines(false);
    this.TaskListTable.getTableHeader().setReorderingAllowed(false);
    this.TaskListScroll.setViewportView(this.TaskListTable);

    this.TaskList.add(this.TaskListScroll, "Center");

    this.item_insertTask.setMnemonic('I');
    this.item_insertTask.setText("Insert Task");
    this.item_insertTask.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_insertTaskActionPerformed(evt);
      }
    });
    this.userPopUp.add(this.item_insertTask);

    this.item_insertJob.setMnemonic('I');
    this.item_insertJob.setText("Insert Job");
    this.item_insertJob.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_insertJobActionPerformed(evt);
      }
    });
    this.taskPopUp.add(this.item_insertJob);

    this.item_runTask.setMnemonic('u');
    this.item_runTask.setText("Run Task");
    this.item_runTask.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_runTaskActionPerformed(evt);
      }
    });
    this.taskPopUp.add(this.item_runTask);

    this.item_stopTask.setMnemonic('T');
    this.item_stopTask.setText("Stop Task");
    this.item_stopTask.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_stopTaskActionPerformed(evt);
      }
    });
    this.taskPopUp.add(this.item_stopTask);

    this.item_preloadTask.setText("jMenuItem4");
    this.item_preloadTask.setActionCommand("Preload Enable");
    this.item_preloadTask.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_preloadTaskActionPerformed(evt);
      }
    });
    this.taskPopUp.add(this.item_preloadTask);

    this.item_taskProperty.setMnemonic('P');
    this.item_taskProperty.setText("Task Property");
    this.item_taskProperty.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_taskPropertyActionPerformed(evt);
      }
    });
    this.taskPopUp.add(this.item_taskProperty);

    this.item_removeTask.setMnemonic('R');
    this.item_removeTask.setText("Remove Task");
    this.item_removeTask.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_removeTaskActionPerformed(evt);
      }
    });
    this.taskPopUp.add(this.item_removeTask);

    this.item_copyTask.setMnemonic('R');
    this.item_copyTask.setText("Copy Task");
    this.item_copyTask.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_copyTaskActionPerformed(evt);
      }
    });
    this.taskPopUp.add(this.item_copyTask);

    this.item_makeTemplate.setText("Make Template");
    this.item_makeTemplate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_makeTemplateActionPerformed(evt);
      }
    });
    this.taskPopUp.add(this.item_makeTemplate);

    this.item_hidden.setText("Hidden");
    this.item_hidden.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_hiddenActionPerformed(evt);
      }
    });
    this.taskPopUp.add(this.item_hidden);

    this.item_removeJob.setMnemonic('R');
    this.item_removeJob.setText("Remove");
    this.item_removeJob.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_removeJobActionPerformed(evt);
      }
    });
    this.jobPopUp.add(this.item_removeJob);

    this.item_jobProperty.setMnemonic('J');
    this.item_jobProperty.setText("Property");
    this.item_jobProperty.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_jobPropertyActionPerformed(evt);
      }
    });
    this.jobPopUp.add(this.item_jobProperty);

    this.item_parameter.setMnemonic('P');
    this.item_parameter.setText("Parameter");
    this.item_parameter.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_parameterActionPerformed(evt);
      }
    });
    this.jobPopUp.add(this.item_parameter);

    this.item_display.setMnemonic('D');
    this.item_display.setText("Display");
    this.item_display.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_displayActionPerformed(evt);
      }
    });
    this.jobPopUp.add(this.item_display);

    this.refreshMenu.setText("Refresh Navigation Tree");
    this.refreshMenu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.refreshMenuActionPerformed(evt);
      }
    });
    this.refreshPopup.add(this.refreshMenu);

    this.PipeSelectDlg.setTitle("Select Pipe");

    this.PipeSelectTable.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null }, { null, null, null, null }, { null, null, null, null }, { null, null, null, null } }, new String[] { "No", "Pipe Name", "Creation Date", "Ref" })
    {
      Class[] types = { Integer.class, String.class, String.class, String.class };

      boolean[] canEdit = { false, false, false, false };

      public Class getColumnClass(int columnIndex)
      {
        return this.types[columnIndex];
      }

      public boolean isCellEditable(int rowIndex, int columnIndex) {
        return this.canEdit[columnIndex];
      }
    });
    this.jScrollPane10.setViewportView(this.PipeSelectTable);

    this.jLabel10.setText("Page :");

    this.jComboBox1.setModel(new DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5" }));

    this.jButton11.setText("Confirm");
    this.jButton11.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton11ActionPerformed(evt);
      }
    });
    this.jButton12.setText("Close");
    this.jButton12.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton12ActionPerformed(evt);
      }
    });
    GroupLayout PipeSelectDlgLayout = new GroupLayout(this.PipeSelectDlg.getContentPane());
    this.PipeSelectDlg.getContentPane().setLayout(PipeSelectDlgLayout);
    PipeSelectDlgLayout.setHorizontalGroup(PipeSelectDlgLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(PipeSelectDlgLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(PipeSelectDlgLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jScrollPane10, -1, 334, 32767)
      .addGroup(PipeSelectDlgLayout
      .createSequentialGroup()
      .addComponent(this.jLabel10)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jComboBox1, -2, -1, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 99, 32767)
      .addComponent(this.jButton11)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jButton12)))
      .addContainerGap()));

    PipeSelectDlgLayout.linkSize(0, new Component[] { this.jButton11, this.jButton12 });

    PipeSelectDlgLayout.setVerticalGroup(PipeSelectDlgLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.TRAILING, PipeSelectDlgLayout
      .createSequentialGroup()
      .addContainerGap()
      .addComponent(this.jScrollPane10, -1, 183, 32767)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(PipeSelectDlgLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jButton12)
      .addComponent(this.jButton11)
      .addComponent(this.jLabel10)
      .addComponent(this.jComboBox1, -2, -1, -2))
      .addContainerGap()));

    this.jMenuItem3.setText("Create");
    this.jMenuItem3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jMenuItem3ActionPerformed(evt);
      }
    });
    this.PopupMenu2.add(this.jMenuItem3);

    this.item_insertQuery2.setMnemonic('I');
    this.item_insertQuery2.setText("Insert Query");
    this.item_insertQuery2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_insertQuery2ActionPerformed(evt);
      }
    });
    this.taskPopUpForMS.add(this.item_insertQuery2);

    this.item_planGeneration.setText("Plan Genearation");
    this.item_planGeneration.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_planGenerationActionPerformed(evt);
      }
    });
    this.taskPopUpForMS.add(this.item_planGeneration);

    this.item_runTask1.setMnemonic('u');
    this.item_runTask1.setText("Run Task");
    this.item_runTask1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_runTask1item_runTaskActionPerformed(evt);
      }
    });
    this.taskPopUpForMS.add(this.item_runTask1);

    this.item_stopTask1.setMnemonic('T');
    this.item_stopTask1.setText("Stop Task");
    this.item_stopTask1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_stopTask1item_stopTaskActionPerformed(evt);
      }
    });
    this.taskPopUpForMS.add(this.item_stopTask1);

    this.item_taskProperty1.setMnemonic('P');
    this.item_taskProperty1.setText("Task Property");
    this.item_taskProperty1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_taskProperty1item_taskPropertyActionPerformed(evt);
      }
    });
    this.taskPopUpForMS.add(this.item_taskProperty1);

    this.item_removeTask1.setMnemonic('R');
    this.item_removeTask1.setText("Remove Task");
    this.item_removeTask1.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_removeTask1item_removeTaskActionPerformed(evt);
      }
    });
    this.taskPopUpForMS.add(this.item_removeTask1);

    this.item_insert_IP_Table.setMnemonic('I');
    this.item_insert_IP_Table.setText("Insert IP Table");
    this.item_insert_IP_Table.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.item_insert_IP_TableActionPerformed(evt);
      }
    });
    this.ipTablePopUp.add(this.item_insert_IP_Table);
    this.item_insert_IP_Table.getAccessibleContext().setAccessibleName("IP Table");

    this.jLabel13.setFont(new Font("굴림", 0, 14));
    this.jLabel13.setText("Name :");

    this.informationPanel.setBorder(BorderFactory.createTitledBorder("Task Information"));

    this.statusTextField.setEditable(false);

    this.jLabel25.setText("IP Address :");

    this.jLabel27.setText("Status : ");

    this.jLabel28.setText("Heartbeat Port :");

    this.jLabel29.setText("Worker :");

    this.jLabel31.setText("Rmi Suffix :");

    this.jLabel32.setText("Input Ports :");

    this.WorkerComboBox.setModel(new DefaultComboBoxModel(new String[] { "Mapper", "Reducer", "Splitter" }));
    this.WorkerComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.WorkerComboBoxActionPerformed(evt);
      }
    });
    this.jLabel24.setText("EX) EX");

    this.jLabel33.setText("EX) 165.132.121.21");

    this.jLabel34.setText("EX) true");

    this.jLabel35.setText("EX) _split");

    this.jLabel36.setText("EX) true");

    this.jLabel37.setText("EX) true");

    this.jLabel38.setText("EX) true");

    this.jLabel39.setText("EX) true");

    this.jLabel40.setText("EX) true");

    this.jLabel41.setText("EX) true");

    this.jLabel42.setText("EX) true");

    this.jLabel43.setText("EX) true");

    GroupLayout informationPanelLayout = new GroupLayout(this.informationPanel);
    this.informationPanel.setLayout(informationPanelLayout);
    informationPanelLayout.setHorizontalGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(informationPanelLayout
      .createSequentialGroup()
      .addContainerGap(-1, 32767)
      .addGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jLabel28)
      .addComponent(this.jLabel31)
      .addComponent(this.jLabel25)
      .addComponent(this.jLabel32)
      .addComponent(this.jLabel29)
      .addComponent(this.jLabel27))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jLabel43)
      .addComponent(this.jLabel42)
      .addComponent(this.jLabel39)
      .addComponent(this.jLabel38)
      .addComponent(this.inputPortTextField, -2, 197, -2)
      .addComponent(this.WorkerComboBox, -2, 196, -2)
      .addComponent(this.jLabel40)
      .addComponent(this.statusTextField, -2, 197, -2)
      .addComponent(this.jLabel41)
      .addGroup(informationPanelLayout
      .createSequentialGroup()
      .addGap(2, 2, 2)
      .addGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.heartbeatPortTextField, -2, 194, -2)
      .addComponent(this.jLabel35)
      .addComponent(this.jLabel34)
      .addComponent(this.jLabel37)
      .addComponent(this.jLabel36)
      .addGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.TRAILING, false)
      .addComponent(this.rmiSuffixTextField, GroupLayout.Alignment.LEADING)
      .addComponent(this.ipAddressTextField, GroupLayout.Alignment.LEADING)
      .addComponent(this.jLabel33, GroupLayout.Alignment.LEADING, -2, 115, -2)
      .addComponent(this.jLabel24, GroupLayout.Alignment.LEADING, -2, 194, -2)))))));

    informationPanelLayout.setVerticalGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(informationPanelLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.ipAddressTextField, -2, -1, -2)
      .addComponent(this.jLabel25))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jLabel24)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jLabel33)
      .addGap(14, 14, 14)
      .addGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel31)
      .addComponent(this.rmiSuffixTextField, -2, -1, -2))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jLabel34)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jLabel35)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.heartbeatPortTextField, -2, -1, -2)
      .addComponent(this.jLabel28))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.jLabel36)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jLabel37)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.inputPortTextField, -2, -1, -2)
      .addComponent(this.jLabel32))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jLabel38)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jLabel39)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel29)
      .addComponent(this.WorkerComboBox, -2, -1, -2))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jLabel40)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
      .addComponent(this.jLabel42)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(informationPanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel27)
      .addComponent(this.statusTextField, -2, -1, -2))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jLabel41)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jLabel43)
      .addGap(5, 5, 5)));

    this.applyButton.setText("Apply");
    this.applyButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.applyButtonActionPerformed(evt);
      }
    });
    this.removeIPTableButton.setText("Remove");
    this.removeIPTableButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.removeIPTableButtonActionPerformed(evt);
      }
    });
    GroupLayout ipTableTaskInfoPanelLayout = new GroupLayout(this.ipTableTaskInfoPanel);
    this.ipTableTaskInfoPanel.setLayout(ipTableTaskInfoPanelLayout);
    ipTableTaskInfoPanelLayout.setHorizontalGroup(ipTableTaskInfoPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(ipTableTaskInfoPanelLayout
      .createSequentialGroup()
      .addGap(0, 0, 32767)
      .addComponent(this.informationPanel, -2, -1, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.OnwerLabel, -2, 178, -2)
      .addContainerGap())
      .addGroup(ipTableTaskInfoPanelLayout
      .createSequentialGroup()
      .addContainerGap()
      .addComponent(this.jLabel13, -2, 55, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.nameTextField, -2, 120, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.applyButton, -2, 85, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.removeIPTableButton)
      .addContainerGap(-1, 32767)));

    ipTableTaskInfoPanelLayout.setVerticalGroup(ipTableTaskInfoPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.TRAILING, ipTableTaskInfoPanelLayout
      .createSequentialGroup()
      .addGroup(ipTableTaskInfoPanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel13, -2, 26, -2)
      .addComponent(this.nameTextField, -2, -1, -2)
      .addComponent(this.applyButton)
      .addComponent(this.removeIPTableButton))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
      .addGroup(ipTableTaskInfoPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.informationPanel, -2, -1, -2)
      .addComponent(this.OnwerLabel))
      .addGap(32, 32, 32)));

    this.ipTableTaskListTable.setModel(new DefaultTableModel(new Object[0][], new String[] { "Name", "IP Address", "RMI Suffix", "Heartbeat Port", "Input Ports", "Worker", "Status" })
    {
      boolean[] canEdit = { false, false, false, false, false, false, false };

      public boolean isCellEditable(int rowIndex, int columnIndex)
      {
        return this.canEdit[columnIndex];
      }
    });
    this.jScrollPane12.setViewportView(this.ipTableTaskListTable);
    if (this.ipTableTaskListTable.getColumnModel().getColumnCount() > 0) {
      this.ipTableTaskListTable.getColumnModel().getColumn(0).setPreferredWidth(60);
      this.ipTableTaskListTable.getColumnModel().getColumn(1).setPreferredWidth(110);
      this.ipTableTaskListTable.getColumnModel().getColumn(5).setPreferredWidth(50);
      this.ipTableTaskListTable.getColumnModel().getColumn(6).setPreferredWidth(50);
    }

    this.jButton13.setText("Off");
    this.jButton13.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton13ActionPerformed(evt);
      }
    });
    this.jButton14.setText("On");
    this.jButton14.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton14ActionPerformed(evt);
      }
    });
    this.jLabel26.setText("1. IP Table Status Timer Controller : ");

    this.jLabel30.setText("2. Timer Period :");

    this.TimerPeriodTextField.setText("10000");

    this.jButton15.setText("Apply");
    this.jButton15.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton15ActionPerformed(evt);
      }
    });
    this.IP_TableRefreshButton.setText("IP Table Refresh");
    this.IP_TableRefreshButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.IP_TableRefreshButtonActionPerformed(evt);
      }
    });
    GroupLayout ipTableTaskListPanelLayout = new GroupLayout(this.ipTableTaskListPanel);
    this.ipTableTaskListPanel.setLayout(ipTableTaskListPanelLayout);
    ipTableTaskListPanelLayout.setHorizontalGroup(ipTableTaskListPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(ipTableTaskListPanelLayout
      .createSequentialGroup()
      .addGap(4, 4, 4)
      .addGroup(ipTableTaskListPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(ipTableTaskListPanelLayout
      .createSequentialGroup()
      .addComponent(this.jScrollPane12, -2, 565, -2)
      .addContainerGap(-1, 32767))
      .addGroup(ipTableTaskListPanelLayout
      .createSequentialGroup()
      .addGroup(ipTableTaskListPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jLabel26)
      .addGroup(ipTableTaskListPanelLayout
      .createSequentialGroup()
      .addComponent(this.jLabel30)
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.TimerPeriodTextField, -2, 51, -2)))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(ipTableTaskListPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jButton15)
      .addGroup(ipTableTaskListPanelLayout
      .createSequentialGroup()
      .addComponent(this.jButton14, -2, 49, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jButton13)))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, 32767)
      .addComponent(this.IP_TableRefreshButton)
      .addContainerGap(-1, 32767)))));

    ipTableTaskListPanelLayout.setVerticalGroup(ipTableTaskListPanelLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(ipTableTaskListPanelLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(ipTableTaskListPanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jButton14)
      .addComponent(this.jButton13)
      .addComponent(this.jLabel26))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(ipTableTaskListPanelLayout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel30)
      .addComponent(this.TimerPeriodTextField, -2, -1, -2)
      .addComponent(this.jButton15)
      .addComponent(this.IP_TableRefreshButton, -1, -1, 32767))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jScrollPane12, -2, -1, -2)
      .addContainerGap()));

    this.jLabel23.setText("ex");

    this.jLabel15.setText("ex");

    this.systemMonitorDialog.setDefaultCloseOperation(2);
    this.systemMonitorDialog.setTitle("SystemMonitorDialog");
    this.systemMonitorDialog.setMinimumSize(new Dimension(680, 450));
    this.systemMonitorDialog.setName("System Information Monitor");
    this.systemMonitorDialog.setResizable(false);
    this.systemMonitorDialog.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent evt) {
        iesMainFrm.this.systemMonitorDialogWindowClosed(evt);
      }
    });
    this.jPanel2.setBorder(BorderFactory.createTitledBorder(null, "System Information Monitor", 0, 0, new Font("굴림", 0, 12), new Color(51, 51, 255)));
    this.jPanel2.setMinimumSize(new Dimension(650, 400));
    this.jPanel2.setPreferredSize(new Dimension(650, 400));

    this.memoryUsageLabel.setText("0.0");

    this.cpuUsageLabel.setText("0.0");

    this.systemTimeLabel.setText("0.0");

    this.jLabel46.setText("System Time :");

    this.jLabel47.setText("CPU Usage :");

    this.endTimeLabel.setText("0.0");

    this.jLabel48.setText("End Time :");

    this.jLabel49.setText("Memory Usage :");

    this.jLabel50.setText("Start Time :");

    this.jLabel51.setText("Input Rate :");

    this.inputRateLabel.setText("0.0");

    this.jLabel52.setText("[Task Name] :");

    this.startTimeTextField.setBackground(new Color(240, 240, 240));
    this.startTimeTextField.setText("0.0");
    this.startTimeTextField.setDragEnabled(true);

    this.jLabel55.setText("Elapsed Time :");

    this.elapsedTimeLabel.setText("0.0");

    this.jLabel57.setText("Queue Info :");

    this.queueInfoLabel.setText("0");

    this.jLabel58.setText("Execution State :");

    this.executionStateLabel.setText("0");

    GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
    this.jPanel2.setLayout(jPanel2Layout);
    jPanel2Layout.setHorizontalGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout
      .createSequentialGroup()
      .addGap(15, 15, 15)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addComponent(this.jLabel58)
      .addComponent(this.jLabel57)
      .addComponent(this.jLabel55)
      .addComponent(this.jLabel52)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addComponent(this.jLabel47)
      .addComponent(this.jLabel49))
      .addComponent(this.jLabel51, GroupLayout.Alignment.TRAILING)
      .addComponent(this.jLabel46, GroupLayout.Alignment.TRAILING)
      .addComponent(this.jLabel50, GroupLayout.Alignment.TRAILING))
      .addComponent(this.jLabel48))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.startTimeTextField)
      .addGroup(jPanel2Layout
      .createSequentialGroup()
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.inputRateLabel)
      .addComponent(this.systemTimeLabel)
      .addComponent(this.memoryUsageLabel)
      .addComponent(this.endTimeLabel, -2, 481, -2)
      .addComponent(this.cpuUsageLabel)
      .addComponent(this.elapsedTimeLabel)
      .addComponent(this.taskNameTextField, -2, 150, -2)
      .addComponent(this.queueInfoLabel)
      .addComponent(this.executionStateLabel))
      .addGap(0, 0, 32767)))
      .addContainerGap()));

    jPanel2Layout.setVerticalGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel2Layout
      .createSequentialGroup()
      .addGap(14, 14, 14)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addComponent(this.cpuUsageLabel)
      .addGroup(jPanel2Layout
      .createSequentialGroup()
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel52)
      .addComponent(this.taskNameTextField, -2, -1, -2))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addComponent(this.jLabel47)))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel49)
      .addComponent(this.memoryUsageLabel))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel51)
      .addComponent(this.inputRateLabel))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel46)
      .addComponent(this.systemTimeLabel))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jLabel50)
      .addComponent(this.startTimeTextField, -2, -1, -2))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel48)
      .addComponent(this.endTimeLabel))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel55)
      .addComponent(this.elapsedTimeLabel))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel57)
      .addComponent(this.queueInfoLabel))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(jPanel2Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel58)
      .addComponent(this.executionStateLabel))
      .addContainerGap(-1, 32767)));

    this.jPanel7.setBorder(BorderFactory.createTitledBorder(null, "System Information Monitor Controller", 0, 0, new Font("굴림", 0, 12), new Color(51, 51, 255)));

    this.autoTerminateChangeTextField.setText("0");

    this.autoTerminateRepeatTextField.setText("0");

    this.autoTerminateOutputCountLabel.setText("(change count: 0)");

    this.autoTerminateTupleRepeatLabel.setText("(repeat count: 0) ");

    this.exportButton.setText("Start Export");
    this.exportButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.exportButtonActionPerformed(evt);
      }
    });
    this.jLabel53.setText("Export Location :");

    this.jLabel54.setText("Auto Terminate :");

    this.exportLocationTextField.setText("C:\\\\Users\\\\fisxher\\\\Desktop\\\\");

    this.viewSystemMonitorButton.setText("View Graph");
    this.viewSystemMonitorButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.viewSystemMonitorButtonActionPerformed(evt);
      }
    });
    this.mapReduceSIMComboBox.setAutoscrolls(true);
    this.mapReduceSIMComboBox.setEnabled(false);
    this.mapReduceSIMComboBox.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.mapReduceSIMComboBoxActionPerformed(evt);
      }
    });
    this.jLabel56.setText("Map Reduce :");

    GroupLayout jPanel7Layout = new GroupLayout(this.jPanel7);
    this.jPanel7.setLayout(jPanel7Layout);
    jPanel7Layout.setHorizontalGroup(jPanel7Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel7Layout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(jPanel7Layout
      .createParallelGroup(GroupLayout.Alignment.TRAILING)
      .addComponent(this.jLabel53)
      .addComponent(this.jLabel54)
      .addComponent(this.jLabel56))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(jPanel7Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel7Layout
      .createSequentialGroup()
      .addGroup(jPanel7Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(jPanel7Layout
      .createSequentialGroup()
      .addGap(201, 201, 201)
      .addComponent(this.autoTerminateChangeTextField, -2, 38, -2))
      .addGroup(jPanel7Layout
      .createSequentialGroup()
      .addComponent(this.autoTerminateRepeatTextField, -2, 38, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.autoTerminateTupleRepeatLabel)))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.autoTerminateOutputCountLabel))
      .addGroup(jPanel7Layout
      .createSequentialGroup()
      .addGroup(jPanel7Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING, false)
      .addComponent(this.mapReduceSIMComboBox, 0, -1, 32767)
      .addComponent(this.exportLocationTextField, -1, 279, 32767))
      .addGap(18, 18, 18)
      .addGroup(jPanel7Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING, false)
      .addComponent(this.viewSystemMonitorButton, -2, 98, -2)
      .addComponent(this.exportButton, -1, -1, 32767))))
      .addGap(0, 121, 32767)));

    jPanel7Layout.setVerticalGroup(jPanel7Layout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(GroupLayout.Alignment.TRAILING, jPanel7Layout
      .createSequentialGroup()
      .addContainerGap(-1, 32767)
      .addGroup(jPanel7Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel56)
      .addComponent(this.mapReduceSIMComboBox, -2, 23, -2)
      .addComponent(this.viewSystemMonitorButton))
      .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
      .addGroup(jPanel7Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.exportLocationTextField, -2, -1, -2)
      .addComponent(this.exportButton)
      .addComponent(this.jLabel53))
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addGroup(jPanel7Layout
      .createParallelGroup(GroupLayout.Alignment.BASELINE)
      .addComponent(this.jLabel54)
      .addComponent(this.autoTerminateRepeatTextField, -2, -1, -2)
      .addComponent(this.autoTerminateChangeTextField, -2, -1, -2)
      .addComponent(this.autoTerminateTupleRepeatLabel)
      .addComponent(this.autoTerminateOutputCountLabel))
      .addContainerGap()));

    GroupLayout systemMonitorDialogLayout = new GroupLayout(this.systemMonitorDialog.getContentPane());
    this.systemMonitorDialog.getContentPane().setLayout(systemMonitorDialogLayout);
    systemMonitorDialogLayout.setHorizontalGroup(systemMonitorDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(systemMonitorDialogLayout
      .createSequentialGroup()
      .addContainerGap()
      .addGroup(systemMonitorDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addComponent(this.jPanel2, -2, 641, -2)
      .addComponent(this.jPanel7, -2, -1, -2))
      .addContainerGap(-1, 32767)));

    systemMonitorDialogLayout.setVerticalGroup(systemMonitorDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGroup(systemMonitorDialogLayout
      .createSequentialGroup()
      .addComponent(this.jPanel2, -2, 293, -2)
      .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
      .addComponent(this.jPanel7, -2, -1, -2)
      .addGap(12, 12, 12)));

    this.systemMonitorViewDialog.setDefaultCloseOperation(2);
    this.systemMonitorViewDialog.setMinimumSize(new Dimension(500, 300));
    this.systemMonitorViewDialog.addWindowListener(new WindowAdapter() {
      public void windowClosed(WindowEvent evt) {
        iesMainFrm.this.systemMonitorViewDialogWindowClosed(evt);
      }
    });
    GroupLayout systemMonitorViewDialogLayout = new GroupLayout(this.systemMonitorViewDialog.getContentPane());
    this.systemMonitorViewDialog.getContentPane().setLayout(systemMonitorViewDialogLayout);
    systemMonitorViewDialogLayout.setHorizontalGroup(systemMonitorViewDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGap(0, 500, 32767));

    systemMonitorViewDialogLayout.setVerticalGroup(systemMonitorViewDialogLayout
      .createParallelGroup(GroupLayout.Alignment.LEADING)
      .addGap(0, 300, 32767));

    setDefaultCloseOperation(3);
    setTitle("Stream Integration Environment System");
    setMinimumSize(new Dimension(800, 600));
    setName("IESMainFrame");

    this.MainFrame.setDividerLocation(200);
    this.MainFrame.setDividerSize(3);
    this.MainFrame.setResizeWeight(0.5D);
    this.MainFrame.setAutoscrolls(true);
    this.MainFrame.setContinuousLayout(true);
    this.MainFrame.setOneTouchExpandable(true);
    this.MainFrame.setPreferredSize(new Dimension(800, 540));

    this.Navis.setDividerLocation(280);
    this.Navis.setDividerSize(2);
    this.Navis.setOrientation(0);
    this.Navis.setResizeWeight(0.5D);
    this.Navis.setContinuousLayout(true);
    this.Navis.setMinimumSize(new Dimension(100, 200));
    this.Navis.setOneTouchExpandable(true);
    this.Navis.setPreferredSize(new Dimension(300, 700));

    this.NaviFrame.setDividerSize(0);
    this.NaviFrame.setOrientation(0);

    this.jPanel1.setBackground(new Color(0, 0, 0));
    this.jPanel1.setLayout(new BorderLayout());

    this.NaviLabel.setFont(new Font("굴림", 1, 12));
    this.NaviLabel.setForeground(new Color(255, 255, 255));
    this.NaviLabel.setText(" Navigator");
    this.jPanel1.add(this.NaviLabel, "Center");

    this.NaviFrame.setTopComponent(this.jPanel1);

    DefaultMutableTreeNode treeNode1 = new DefaultMutableTreeNode("root");
    this.NaviTree.setModel(new DefaultTreeModel(treeNode1));
    this.NaviTree.addMouseListener(new MouseAdapter() {
      public void mouseClicked(MouseEvent evt) {
        iesMainFrm.this.NaviTreeMouseClicked(evt);
      }
      public void mouseReleased(MouseEvent evt) {
        iesMainFrm.this.NaviTreeMouseReleased(evt);
      }
    });
    this.NaviTree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent evt) {
        iesMainFrm.this.NaviTreeValueChanged(evt);
      }
    });
    this.NaviScroll.setViewportView(this.NaviTree);

    this.NaviFrame.setRightComponent(this.NaviScroll);

    this.Navis.setTopComponent(this.NaviFrame);

    this.TasksFrame.setDividerSize(0);
    this.TasksFrame.setOrientation(0);
    this.TasksFrame.setAutoscrolls(true);

    this.jPanel3.setBackground(new Color(0, 0, 0));
    this.jPanel3.setLayout(new BorderLayout());

    this.jLabel2.setFont(new Font("굴림", 1, 12));
    this.jLabel2.setForeground(new Color(255, 255, 255));
    this.jLabel2.setText(" Activity Tasks");
    this.jPanel3.add(this.jLabel2, "Center");

    this.TasksFrame.setTopComponent(this.jPanel3);

    this.TasksTable1.setModel(new DefaultTableModel(new Object[0][], new String[] { "Name", "Status" })
    {
      boolean[] canEdit = { false, false };

      public boolean isCellEditable(int rowIndex, int columnIndex)
      {
        return this.canEdit[columnIndex];
      }
    });
    this.TasksTable1.setFillsViewportHeight(true);
    this.TasksTable1.setGridColor(new Color(255, 255, 255));
    this.TasksScroll.setViewportView(this.TasksTable1);

    this.TasksFrame.setBottomComponent(this.TasksScroll);

    this.Navis.setRightComponent(this.TasksFrame);

    this.MainFrame.setLeftComponent(this.Navis);

    this.Controllers.setLayout(new BorderLayout());
    this.jScrollPane11.setViewportView(this.Controllers);

    this.MainFrame.setRightComponent(this.jScrollPane11);

    getContentPane().add(this.MainFrame, "Center");

    this.jToolBar1.setRollover(true);
    this.jToolBar1.setBorderPainted(false);

    this.jButton2.setIcon(this.add_ddl);
    this.jButton2.setToolTipText("Add DDL");
    this.jButton2.setBorderPainted(false);
    this.jButton2.setDisabledSelectedIcon(this.add_ddl);
    this.jButton2.setFocusable(false);
    this.jButton2.setHorizontalTextPosition(0);
    this.jButton2.setPressedIcon(this.add_ddl);
    this.jButton2.setSelectedIcon(this.add_ddl);
    this.jButton2.setVerticalTextPosition(3);
    this.jButton2.setDisabledIcon(this.add_ddl);
    this.jButton2.setRolloverIcon(this.add_ddl);
    this.jButton2.setRolloverSelectedIcon(this.add_ddl);
    this.jButton2.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton2ActionPerformed(evt);
      }
    });
    this.jToolBar1.add(this.jButton2);

    this.jButton9.setIcon(this.add_task);
    this.jButton9.setToolTipText("Add Task");
    this.jButton9.setBorderPainted(false);
    this.jButton9.setDisabledIcon(this.add_task);
    this.jButton9.setDisabledSelectedIcon(this.add_task);
    this.jButton9.setFocusable(false);
    this.jButton9.setHorizontalTextPosition(0);
    this.jButton9.setPressedIcon(this.add_task);
    this.jButton9.setRolloverIcon(this.add_task);
    this.jButton9.setRolloverSelectedIcon(this.add_task);
    this.jButton9.setSelectedIcon(this.add_task);
    this.jButton9.setVerticalTextPosition(3);
    this.jButton9.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.jButton9ActionPerformed(evt);
      }
    });
    this.jToolBar1.add(this.jButton9);
    this.jToolBar1.add(this.jSeparator2);

    this.jLabel45.setText("[Local] ");
    this.jToolBar1.add(this.jLabel45);

    this.TaskRunTBtn.setIcon(this.run);
    this.TaskRunTBtn.setToolTipText("Run current Task");
    this.TaskRunTBtn.setBorderPainted(false);
    this.TaskRunTBtn.setDisabledIcon(this.run_d);
    this.TaskRunTBtn.setDisabledSelectedIcon(this.run_d);
    this.TaskRunTBtn.setEnabled(false);
    this.TaskRunTBtn.setFocusable(false);
    this.TaskRunTBtn.setHorizontalTextPosition(0);
    this.TaskRunTBtn.setPressedIcon(this.run);
    this.TaskRunTBtn.setRolloverIcon(this.run);
    this.TaskRunTBtn.setRolloverSelectedIcon(this.run);
    this.TaskRunTBtn.setSelectedIcon(this.run);
    this.TaskRunTBtn.setVerticalTextPosition(3);
    this.TaskRunTBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.TaskRunTBtnActionPerformed(evt);
      }
    });
    this.jToolBar1.add(this.TaskRunTBtn);

    this.TaskStopTBtn.setIcon(this.stop);
    this.TaskStopTBtn.setToolTipText("Stop current Task");
    this.TaskStopTBtn.setBorderPainted(false);
    this.TaskStopTBtn.setDisabledIcon(this.stop_d);
    this.TaskStopTBtn.setDisabledSelectedIcon(this.stop_d);
    this.TaskStopTBtn.setEnabled(false);
    this.TaskStopTBtn.setFocusable(false);
    this.TaskStopTBtn.setHorizontalTextPosition(0);
    this.TaskStopTBtn.setPressedIcon(this.stop);
    this.TaskStopTBtn.setRolloverIcon(this.stop);
    this.TaskStopTBtn.setRolloverSelectedIcon(this.stop);
    this.TaskStopTBtn.setSelectedIcon(this.stop);
    this.TaskStopTBtn.setVerticalTextPosition(3);
    this.TaskStopTBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.TaskStopTBtnActionPerformed(evt);
      }
    });
    this.jToolBar1.add(this.TaskStopTBtn);

    this.TaskRerunTBtn.setIcon(this.rerun);
    this.TaskRerunTBtn.setToolTipText("Rerun current Task");
    this.TaskRerunTBtn.setBorderPainted(false);
    this.TaskRerunTBtn.setDisabledIcon(this.rerun_d);
    this.TaskRerunTBtn.setDisabledSelectedIcon(this.rerun_d);
    this.TaskRerunTBtn.setEnabled(false);
    this.TaskRerunTBtn.setFocusable(false);
    this.TaskRerunTBtn.setHorizontalTextPosition(0);
    this.TaskRerunTBtn.setPressedIcon(this.rerun);
    this.TaskRerunTBtn.setRolloverIcon(this.rerun);
    this.TaskRerunTBtn.setRolloverSelectedIcon(this.rerun);
    this.TaskRerunTBtn.setSelectedIcon(this.rerun);
    this.TaskRerunTBtn.setVerticalTextPosition(3);
    this.TaskRerunTBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.TaskRerunTBtnActionPerformed(evt);
      }
    });
    this.jToolBar1.add(this.TaskRerunTBtn);

    this.TaskPauseTBtn.setIcon(this.pause);
    this.TaskPauseTBtn.setToolTipText("Pause current Task");
    this.TaskPauseTBtn.setBorderPainted(false);
    this.TaskPauseTBtn.setDisabledIcon(this.pause_d);
    this.TaskPauseTBtn.setDisabledSelectedIcon(this.pause_d);
    this.TaskPauseTBtn.setEnabled(false);
    this.TaskPauseTBtn.setFocusable(false);
    this.TaskPauseTBtn.setHorizontalTextPosition(0);
    this.TaskPauseTBtn.setPressedIcon(this.pause);
    this.TaskPauseTBtn.setRolloverIcon(this.pause);
    this.TaskPauseTBtn.setRolloverSelectedIcon(this.pause);
    this.TaskPauseTBtn.setSelectedIcon(this.pause);
    this.TaskPauseTBtn.setVerticalTextPosition(3);
    this.TaskPauseTBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.TaskPauseTBtnActionPerformed(evt);
      }
    });
    this.jToolBar1.add(this.TaskPauseTBtn);
    this.jToolBar1.add(this.jSeparator3);

    this.jLabel44.setText("[MapReduce] ");
    this.jToolBar1.add(this.jLabel44);

    this.MapReduceRunBtn.setIcon(this.run);
    this.MapReduceRunBtn.setToolTipText("Run All Tasks on MapReduce");
    this.MapReduceRunBtn.setBorderPainted(false);
    this.MapReduceRunBtn.setDisabledIcon(this.run_d);
    this.MapReduceRunBtn.setDisabledSelectedIcon(this.run_d);
    this.MapReduceRunBtn.setEnabled(false);
    this.MapReduceRunBtn.setFocusable(false);
    this.MapReduceRunBtn.setHorizontalTextPosition(0);
    this.MapReduceRunBtn.setPressedIcon(this.run);
    this.MapReduceRunBtn.setRolloverIcon(this.run);
    this.MapReduceRunBtn.setRolloverSelectedIcon(this.run);
    this.MapReduceRunBtn.setSelectedIcon(this.run);
    this.MapReduceRunBtn.setVerticalTextPosition(3);
    this.MapReduceRunBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.MapReduceRunBtnActionPerformed(evt);
      }
    });
    this.jToolBar1.add(this.MapReduceRunBtn);

    this.MapReduceStopBtn.setIcon(this.stop);
    this.MapReduceStopBtn.setToolTipText("Stop All Tasks on MapReduce");
    this.MapReduceStopBtn.setBorderPainted(false);
    this.MapReduceStopBtn.setDisabledIcon(this.stop_d);
    this.MapReduceStopBtn.setDisabledSelectedIcon(this.stop_d);
    this.MapReduceStopBtn.setEnabled(false);
    this.MapReduceStopBtn.setFocusable(false);
    this.MapReduceStopBtn.setHorizontalTextPosition(0);
    this.MapReduceStopBtn.setPressedIcon(this.stop);
    this.MapReduceStopBtn.setRolloverIcon(this.stop);
    this.MapReduceStopBtn.setRolloverSelectedIcon(this.stop);
    this.MapReduceStopBtn.setSelectedIcon(this.stop);
    this.MapReduceStopBtn.setVerticalTextPosition(3);
    this.MapReduceStopBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.MapReduceStopBtnActionPerformed(evt);
      }
    });
    this.jToolBar1.add(this.MapReduceStopBtn);
    this.jToolBar1.add(this.jSeparator4);

    this.systemMonitorButton.setText("Monitor");
    this.systemMonitorButton.setToolTipText("View System Information Monitor");
    this.systemMonitorButton.setFocusable(false);
    this.systemMonitorButton.setHorizontalTextPosition(0);
    this.systemMonitorButton.setVerticalTextPosition(3);
    this.systemMonitorButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        iesMainFrm.this.systemMonitorButtonActionPerformed(evt);
      }
    });
    this.jToolBar1.add(this.systemMonitorButton);

    getContentPane().add(this.jToolBar1, "First");

    this.jMenu1.setMnemonic('F');
    this.jMenu1.setText("File");
    this.MenuBar.add(this.jMenu1);

    this.jMenu2.setMnemonic('V');
    this.jMenu2.setText("View");
    this.MenuBar.add(this.jMenu2);

    this.jMenu3.setMnemonic('D');
    this.jMenu3.setText("Data");
    this.MenuBar.add(this.jMenu3);

    this.jMenu4.setMnemonic('A');
    this.jMenu4.setText("Activity");
    this.MenuBar.add(this.jMenu4);

    this.jMenu5.setMnemonic('T');
    this.jMenu5.setText("Tools");
    this.MenuBar.add(this.jMenu5);

    this.jMenu6.setMnemonic('H');
    this.jMenu6.setText("Help");
    this.MenuBar.add(this.jMenu6);

    setJMenuBar(this.MenuBar);

    pack();
  }

  private void jButton1ActionPerformed(ActionEvent evt)
  {
  }

  private void VFFieldDelimeterActionPerformed(ActionEvent evt)
  {
  }

  private void VFRecordDelimeterActionPerformed(ActionEvent evt)
  {
  }

  private void jButton3ActionPerformed(ActionEvent evt) {
    if (confirmVariable())
    {
      this.VariableDialog.setVisible(false);
    }
  }

  private void jButton4ActionPerformed(ActionEvent evt)
  {
    this.VariableDialog.setVisible(false);
  }

  private void FieldAddNullActionPerformed(ActionEvent evt)
  {
  }

  public boolean isMasterOfMapReduce(long taskId) throws RemoteException {
    String str = null;
    if (((str = this.tl.getMapReduceBasicInfo(taskId, "type")) != null) &&
      (str.equals("master")) &&
      (RMI_Controller.checkSlavesAlive(this.tl
      .getMapReduceMasterInfo(taskId, "splitter-ips", "splitter-ip", "ip"),
      this.tl
      .getMapReduceMasterInfo(taskId, "splitter-suffixes", "splitter-suffix", "suffix"))))
    {
      if (RMI_Controller.checkSlavesAlive(this.tl
        .getMapReduceMasterInfo(taskId, "mapper-ips", "mapper-ip", "ip"),
        this.tl
        .getMapReduceMasterInfo(taskId, "mapper-suffixes", "mapper-suffix", "suffix")))
      {
        if (RMI_Controller.checkSlavesAlive(this.tl
          .getMapReduceMasterInfo(taskId, "reducer-ips", "reducer-ip", "ip"),
          this.tl
          .getMapReduceMasterInfo(taskId, "reducer-suffixes", "reducer-suffix", "suffix")))
        {
          return true;
        }
      }
    }
    return false;
  }

  private void NaviTreeValueChanged(TreeSelectionEvent evt)
  {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.NaviTree.getLastSelectedPathComponent();

    if (node == null)
    {
      return;
    }

    TreeModel test = this.NaviTree.getModel();
    NodeInfo type = (NodeInfo)node.getUserObject();

    this.popnode = type;

    if (type.getType().equals("task"))
    {
      try
      {
        TaskState state = this.doo.currentState(type.getLongId());
        switchTaskToolBar(state);

        if ((type.getLongId() != this.cur_taskId) || (this.cur_view != this.job_viewer))
        {
          long taskId = type.getLongId();
          String str = null;

          if ((str = this.tl.getMapReduceBasicInfo(taskId, "type")) != null) {
            if ((str.equals("master")) &&
              (RMI_Controller.checkSlavesAlive(this.tl
              .getMapReduceMasterInfo(taskId, "splitter-ips", "splitter-ip", "ip"),
              this.tl
              .getMapReduceMasterInfo(taskId, "splitter-suffixes", "splitter-suffix", "suffix"))))
            {
              if (RMI_Controller.checkSlavesAlive(this.tl
                .getMapReduceMasterInfo(taskId, "mapper-ips", "mapper-ip", "ip"),
                this.tl
                .getMapReduceMasterInfo(taskId, "mapper-suffixes", "mapper-suffix", "suffix")))
              {
                if (RMI_Controller.checkSlavesAlive(this.tl
                  .getMapReduceMasterInfo(taskId, "reducer-ips", "reducer-ip", "ip"),
                  this.tl
                  .getMapReduceMasterInfo(taskId, "reducer-suffixes", "reducer-suffix", "suffix")))
                {
                  switchMapReduceTaskToolBar(state);
                  if ((this.tl.getMapReduceMasterInfo(taskId, "splitter-ips", "splitter-ip", "ip") != null) &&
                    (this.tl
                    .getMapReduceMasterInfo(taskId, "splitter-suffixes", "splitter-suffix", "suffix") != null))
                  {
                    RMI_Controller.initSplitterRMI_Operations(this.tl.getMapReduceMasterInfo(taskId, "splitter-ips", "splitter-ip", "ip"), this.tl
                      .getMapReduceMasterInfo(taskId, "splitter-suffixes", "splitter-suffix", "suffix"));
                  }

                  if ((this.tl.getMapReduceMasterInfo(taskId, "mapper-ips", "mapper-ip", "ip") != null) &&
                    (this.tl
                    .getMapReduceMasterInfo(taskId, "mapper-suffixes", "mapper-suffix", "suffix") != null))
                  {
                    RMI_Controller.initMapperRMI_Operations(this.tl.getMapReduceMasterInfo(taskId, "mapper-ips", "mapper-ip", "ip"), this.tl
                      .getMapReduceMasterInfo(taskId, "mapper-suffixes", "mapper-suffix", "suffix"));
                  }

                  if ((this.tl.getMapReduceMasterInfo(taskId, "reducer-ips", "reducer-ip", "ip") == null) ||
                    (this.tl
                    .getMapReduceMasterInfo(taskId, "reducer-suffixes", "reducer-suffix", "suffix") == null))
                    break label589;
                  RMI_Controller.initReducerRMI_Operations(this.tl.getMapReduceMasterInfo(taskId, "reducer-ips", "reducer-ip", "ip"), this.tl
                    .getMapReduceMasterInfo(taskId, "reducer-suffixes", "reducer-suffix", "suffix")); break label589;
                }
              }
            }

            this.MapReduceRunBtn.setEnabled(false);
            this.MapReduceStopBtn.setEnabled(false);
          }
          else
          {
            this.MapReduceRunBtn.setEnabled(false);
            this.MapReduceStopBtn.setEnabled(false);
          }
          label589: this.cur_taskId = taskId;
          NodeInfo[] jobs = this.tl.getJobList(taskId);
          this.job_viewer = new JobViewer(this.cur_taskId);
          this.job_viewer.setParent(this);
          if (!this.tl.getTaskHidden(taskId)) {
            for (int i = 0; i < jobs.length; i++) {
              try {
                Object testnode = test.getChild(node, i);

                this.job_viewer.addButton(new Job(jobs[i].getLongId(), jobs[i].getIdentifier(), jobs[i].getName(), false), (TreeNode)testnode);
              } catch (UnsupportedEncodingException ex) {
                log.debug(ex);
              }
            }

            this.job_viewer.initLines();
          }
          this.cur_view.setVisible(false);
          this.Controllers.remove(this.cur_view);
          this.cur_view = this.job_viewer;
          this.Controllers.add(this.cur_view, "Center");

          this.cur_view.setVisible(true);
        }

        return;
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (ExecutionTerminationException ex)
      {
        log.debug(ex);
        warnMsgDlg(this, ex.getMessage());
      }
    }
    else if (type.getType().equals("job"))
    {
      DefaultMutableTreeNode pNode = (DefaultMutableTreeNode)node.getParent();

      NodeInfo parent = (NodeInfo)pNode.getUserObject();
      try
      {
        TaskState state = this.doo.currentState(parent.getLongId());
        switchTaskToolBar(state);

        if ((parent.getLongId() != this.cur_taskId) || (this.cur_view != this.job_viewer))
        {
          long taskId = parent.getLongId();
          this.cur_taskId = taskId;
          NodeInfo[] jobs = null;

          jobs = this.tl.getJobList(taskId);

          this.job_viewer = new JobViewer(this.cur_taskId);
          this.job_viewer.setParent(this);

          for (int i = 0; i < jobs.length; i++)
          {
            Object testnode = test.getChild(pNode, i);
            this.job_viewer.addButton(new Job(jobs[i].getLongId(), jobs[i].getIdentifier(), jobs[i].getName(), false), (TreeNode)testnode);
          }

          this.job_viewer.initLines();
          this.cur_view.setVisible(false);
          this.Controllers.remove(this.cur_view);
          this.cur_view = this.job_viewer;
          this.Controllers.add(this.cur_view, "Center");
          this.cur_view.setVisible(true);
        }

        return;
      }
      catch (RemoteException ex)
      {
        log.debug(ex);
        this.tl = iesRMI.getTPOFromRMI();
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (ExecutionTerminationException ex)
      {
        log.debug(ex);
        warnMsgDlg(this, ex.getMessage());
      }
    }

    this.cur_view.setVisible(false);
    this.Controllers.remove(this.cur_view);

    if (type.getType().equals("db"))
    {
      this.cur_view = this.DBMainList;
      switchTaskToolBar(null);
    }
    else if (type.getType().equals("dd"))
    {
      try
      {
        initUserList();
        this.cur_view = this.UserList;
        switchTaskToolBar(null);
      }
      catch (RemoteException ex)
      {
        log.debug(ex);
        this.ol = iesRMI.getDOFromRMI();
      }
    }
    else if (type.getType().equals("user"))
    {
      try
      {
        initDDLList(type.getName());
        this.cur_view = this.DDLList;
        switchTaskToolBar(null);
      }
      catch (RemoteException ex)
      {
        log.debug(ex);
        this.ol = iesRMI.getDOFromRMI();
      }
    }
    else if (type.getType().equals("ddl"))
    {
      try
      {
        initDDLDetail(type.getId());
        this.cur_ddl_id = type.getId();
        this.cur_user_name = this.current_ddl.getOwner();
        initVariableFrame();
        initFixedFrame();
        this.cur_view = this.DDLDetail;
        switchTaskToolBar(null);
      }
      catch (RemoteException ex)
      {
        log.debug(ex);
        this.ol = iesRMI.getDOFromRMI();
      }
    }
    else if (type.getType().equals("tasks"))
    {
      this.cur_view = this.TaskList;
      switchTaskToolBar(null);
    }
    else if (type.getType().equals("taskuser"))
    {
      try
      {
        initTaskList(type.getName());
        this.cur_view = this.TaskUserList;
        switchTaskToolBar(null);
      }
      catch (RemoteException ex)
      {
        log.debug(ex);
        this.tl = iesRMI.getTPOFromRMI();
      }
    }
    else if (type.getType().equals("iptable"))
    {
      DefaultTableModel dModel = (DefaultTableModel)this.ipTableTaskListTable.getModel();
      try
      {
        ipTableTaskInfo[] tasks = this.ipTableTasks.getAllTaskInfos();
        dModel.setRowCount(tasks.length);

        for (int i = 0; i < tasks.length; i++)
        {
          dModel.setValueAt(tasks[i].getName(), i, 0);
          dModel.setValueAt(tasks[i].getIpAddress(), i, 1);
          dModel.setValueAt(tasks[i].getRmiSuffix(), i, 2);
          dModel.setValueAt(Integer.valueOf(tasks[i].getHeartbeatPort()), i, 3);
          dModel.setValueAt(tasks[i].getInputPorts(), i, 4);
          dModel.setValueAt(tasks[i].getWorker(), i, 5);
          dModel.setValueAt(Boolean.valueOf(tasks[i].getStatus()), i, 6);
        }
        this.cur_view = this.ipTableTaskListPanel;
      }
      catch (Exception localException)
      {
      }
    }
    else if (type.getType().equals("iptabletask"))
    {
      try
      {
        this.cur_task = this.ipTableTasks.getTaskInfoById(type.getLongId());
        this.nameTextField.setText(this.cur_task.getName());
        this.ipAddressTextField.setText(this.cur_task.getIpAddress());
        this.rmiSuffixTextField.setText(String.valueOf(this.cur_task.getRmiSuffix()));
        this.heartbeatPortTextField.setText(String.valueOf(this.cur_task.getHeartbeatPort()));
        this.inputPortTextField.setText(String.valueOf(this.cur_task.getInputPorts()));
        if (this.cur_task.getWorker().equals("Mapper"))
          this.WorkerComboBox.setSelectedIndex(0);
        else if (this.cur_task.getWorker().equals("Reducer"))
          this.WorkerComboBox.setSelectedIndex(1);
        else if (this.cur_task.getWorker().equals("Splitter"))
          this.WorkerComboBox.setSelectedIndex(2);
        this.statusTextField.setText(String.valueOf(this.cur_task.getStatus()));

        this.cur_view = this.ipTableTaskInfoPanel;
      }
      catch (Exception localException1)
      {
      }
    }
    this.Controllers.add(this.cur_view, "Center");
    this.cur_view.setVisible(true);
  }

  private void jTextField1ActionPerformed(ActionEvent evt)
  {
  }

  private void indetailButtonActionPerformed(ActionEvent evt)
  {
    if (this.FieldTypeVariable.isSelected())
    {
      initVariableFrame();
      this.VariableDialog.setLocationRelativeTo(this.MainFrame);
      this.VariableDialog.setSize(new Dimension(500, 400));
      this.VariableDialog.setVisible(true);
    }

    if (this.FieldTypeFixed.isSelected())
    {
      initFixedFrame();
      this.FixedDialog.setLocationRelativeTo(this.MainFrame);
      this.FixedDialog.setSize(new Dimension(500, 300));
      this.FixedDialog.setVisible(true);
    }
  }

  private void FieldTypeVariableActionPerformed(ActionEvent evt)
  {
  }

  private void FieldTypeFixedActionPerformed(ActionEvent evt)
  {
  }

  private void jMenuItem1ActionPerformed(ActionEvent evt)
  {
    try {
      initDDLCreateDialog(this.ol.getDataDefintion(this.popnode.getId()).getOwner());
      this.create_mode = true;
      this.old_ddl = this.current_ddl;
      this.current_ddl = new DataDefinition();
      this.DDLCreateDialog.setLocationRelativeTo(this.MainFrame);
      this.DDLCreateDialog.setSize(new Dimension(600, 410));
      this.DDLCreateDialog.setVisible(true);
    }
    catch (RemoteException ex)
    {
      log.debug(ex);
      this.ol = iesRMI.getDOFromRMI();
    }
  }

  private void NaviTreeMouseClicked(MouseEvent evt)
  {
    try {
      TreePopup(evt);
    }
    catch (RemoteException ex)
    {
      log.debug(ex);
      this.ol = iesRMI.getDOFromRMI();
      this.tl = iesRMI.getTPOFromRMI();
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
  }

  private void NaviTreeMouseReleased(MouseEvent evt)
  {
    try {
      TreePopup(evt);
    }
    catch (RemoteException ex)
    {
      log.debug(ex);
      this.doo = iesRMI.getDeployerFromRMI();
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
  }

  private void jMenuItem2ActionPerformed(ActionEvent evt)
  {
    try {
      String username = this.ol.getDataDefintion(this.popnode.getId()).getOwner();
      int answer = JOptionPane.showConfirmDialog(this.MainFrame, "Drop table " + this.popnode.getName() + " from " + username + ", is it OK?", "Drop Table", 0);
      if (answer == 0)
      {
        this.ol.removeDataDefintion(this.popnode.getId());
        this.ol.rebuildXmlFile(this.out_filename);
        initDDLList(username);
        if ((this.cur_view == this.DDLDetail) && (this.popnode.getId() == this.current_ddl.getId()))
        {
          this.cur_view.setVisible(false);
          this.Controllers.remove(this.cur_view);
          this.cur_view = this.DDLList;
          this.Controllers.add(this.cur_view, "Center");
          this.cur_view.setVisible(true);
        }
        initUserList();
        DefaultTreeModel NaviModel = (DefaultTreeModel)this.NaviTree.getModel();
        NaviModel.removeNodeFromParent(this.datanode);
      }
    }
    catch (RemoteException ex)
    {
      log.debug(ex);
      this.ol = iesRMI.getDOFromRMI();
    }
    catch (TransformerConfigurationException ex)
    {
      log.debug(ex);
    }
    catch (TransformerException ex)
    {
      log.debug(ex);
    }
  }

  private void AddFieldActionPerformed(ActionEvent evt) {
    initFieldAddFrame();
    this.FieldAddDialog.setLocationRelativeTo(this.MainFrame);
    this.FieldAddDialog.setSize(new Dimension(400, 300));
    this.FieldAddDialog.setVisible(true);
  }

  private void RemoveFieldActionPerformed(ActionEvent evt) {
    int answer = JOptionPane.showConfirmDialog(this.MainFrame, "Drop column from " + this.current_ddl.getTitle() + ", is it OK?", "Drop Field", 0);
    if (answer == 0)
    {
      delAttributeField();
    }
  }

  private void CommitFieldActionPerformed(ActionEvent evt)
  {
    int id_flag = 0;
    int ddl_id = this.current_ddl.getId();
    int answer = JOptionPane.showConfirmDialog(this.MainFrame, "Commit changes to database, is it OK?", "Commit", 0);
    ArrayList taskInfo = new ArrayList();
    String taskOutputInfo = "";
    try
    {
      Vector OwnerList = this.tl.getOwnerList();
      NodeInfo[] TaskXmlList = null;
      NodeInfo[] XmlJobList = null;

      for (int i = 0; i < OwnerList.size(); i++)
      {
        if (((String)OwnerList.get(i)).equals(this.loginedName))
        {
          TaskXmlList = this.tl.getTaskList((String)OwnerList.get(i));
          for (int j = 0; j < TaskXmlList.length; j++)
          {
            XmlJobList = this.tl.getJobList(TaskXmlList[j].getLongId());

            for (int k = 0; k < XmlJobList.length; k++)
            {
              if ((XmlJobList[k].getIdentifier() == 1) &&
                (this.tl.getDataDefinitionId(XmlJobList[k].getLongId()) == ddl_id)) {
                id_flag++;
                taskInfo.add(TaskXmlList[j].toString());
              }
            }
          }
        }
      }
    }
    catch (Exception localException) {
    }
    if (id_flag == 1) {
      answer = JOptionPane.showConfirmDialog(this.MainFrame, "Task '" +
        (String)taskInfo
        .get(0) +
        "' is(are) using current DDL.  Really want to commit it?", "Warning", 0);
    }
    else if (id_flag > 1) {
      answer = -1;
      for (int i = 0; i < taskInfo.size(); i++) {
        taskOutputInfo = taskOutputInfo + (String)taskInfo.get(i) + ", ";
      }
      int lastIndex = taskOutputInfo.length();
      JOptionPane.showConfirmDialog(this.MainFrame, "You cannot change this DDL because task '" + taskOutputInfo
        .substring(0, lastIndex - 1) +
        "' is(are) using current DDL ", "Warning", 0);
    }

    if (answer == 0)
    {
      try
      {
        commitDDL();
      }
      catch (TransformerConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (TransformerException ex)
      {
        log.debug(ex);
      }
      catch (RemoteException ex)
      {
        log.info(ex);
        this.ol = iesRMI.getDOFromRMI();
      }
    }
  }

  private void UseItemsetCheckActionPerformed(ActionEvent evt)
  {
    this.ItemsetDifferent.setEnabled(this.UseItemsetCheck.isSelected());
    this.ItemsetDelimeter.setEnabled((this.UseItemsetCheck.isSelected()) && (this.ItemsetDifferent.isSelected()));
    this.ItemsetSame.setEnabled(this.UseItemsetCheck.isSelected());
    this.ItemsetTableScroll.setEnabled(this.UseItemsetCheck.isSelected());
    this.ItemsetTable.setEnabled(this.UseItemsetCheck.isSelected());
  }

  private void ItemsetDifferentActionPerformed(ActionEvent evt) {
    this.ItemsetDelimeter.setEnabled((this.UseItemsetCheck.isSelected()) && (this.ItemsetDifferent.isSelected()));
  }

  private void ItemsetSameActionPerformed(ActionEvent evt) {
    this.ItemsetDelimeter.setEnabled((this.UseItemsetCheck.isSelected()) && (this.ItemsetDifferent.isSelected()));
  }

  private void jButton6ActionPerformed(ActionEvent evt) {
    if (confirmFixed())
    {
      this.FixedDialog.setVisible(false);
    }
  }

  private void jButton5ActionPerformed(ActionEvent evt) {
    this.FixedDialog.setVisible(false);
  }

  private void jButton8ActionPerformed(ActionEvent evt) {
    if (confirmFieldAddFrame())
    {
      this.FieldAddDialog.setVisible(false);
    }
  }

  private void jButton7ActionPerformed(ActionEvent evt) {
    this.FieldAddDialog.setVisible(false);
  }

  private void FieldAddTypeActionPerformed(ActionEvent evt)
  {
  }

  private void CreateFieldTypeVariableActionPerformed(ActionEvent evt)
  {
  }

  private void CreateFieldTypeFixedActionPerformed(ActionEvent evt)
  {
  }

  private void createIndetailButtonActionPerformed(ActionEvent evt) {
    if (this.CreateFieldTypeVariable.isSelected())
    {
      initVariableFrame();
      this.VariableDialog.setLocationRelativeTo(this.DDLCreateDialog);
      this.VariableDialog.setSize(new Dimension(500, 400));
      this.VariableDialog.setVisible(true);
    }

    if (this.CreateFieldTypeFixed.isSelected())
    {
      initFixedFrame();
      this.FixedDialog.setLocationRelativeTo(this.DDLCreateDialog);
      this.FixedDialog.setSize(new Dimension(500, 300));
      this.FixedDialog.setVisible(true);
    }
  }

  private void CreateAddFieldActionPerformed(ActionEvent evt)
  {
    initFieldAddFrame();
    this.FieldAddDialog.setLocationRelativeTo(this.DDLCreateDialog);
    this.FieldAddDialog.setSize(new Dimension(400, 300));
    this.FieldAddDialog.setVisible(true);
  }

  private void CreateRemoveFieldActionPerformed(ActionEvent evt) {
    int answer = JOptionPane.showConfirmDialog(this.DDLCreateDialog, "Drop column from this Data definition, is it OK?", "Drop Field", 0);
    if (answer == 0)
    {
      delAttributeField();
    }
  }

  private void CreateCancleActionPerformed(ActionEvent evt) {
    this.current_ddl = this.old_ddl;
    this.DDLCreateDialog.setVisible(false);
    this.create_mode = false;
  }

  private void CreateOKActionPerformed(ActionEvent evt)
  {
    try {
      if (commitNewDDL())
      {
        this.current_ddl = this.old_ddl;
        this.DDLCreateDialog.setVisible(false);
        this.create_mode = false;
        this.ol.rebuildXmlFile(this.out_filename);
      }
    }
    catch (TransformerConfigurationException ex)
    {
      log.debug(ex);
    }
    catch (TransformerException ex)
    {
      log.debug(ex);
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.ol = iesRMI.getDOFromRMI();
    }
  }

  private void DDLCreateDialogWindowClosed(WindowEvent evt) {
  }

  private void DDLCreateDialogWindowClosing(WindowEvent evt) {
    this.current_ddl = this.old_ddl;
    this.create_mode = false;
  }

  private void DDLCreateTablePropertyChange(PropertyChangeEvent evt) {
    if (!this.DDLCreateTable.isEditing())
    {
      if (!checkAttributeTable())
      {
        initFixedFrame();
        initVariableFrame();
      }
    }
  }

  private void DDLDefTablePropertyChange(PropertyChangeEvent evt) {
    if (!this.DDLDefTable.isEditing())
    {
      if (!checkAttributeTable())
      {
        initFixedFrame();
        initVariableFrame();
      }
    }
  }

  private void item_insertTaskActionPerformed(ActionEvent evt)
  {
    TaskInsertion dlg = new TaskInsertion(this, true);
    dlg.setLocationRelativeTo(this.MainFrame);
    dlg.setVisible(true);
  }

  private void item_insertJobActionPerformed(ActionEvent evt)
  {
    try
    {
      SelectJobType dlg = new SelectJobType(this, this.tl.getTaskType(this.popnode.getLongId()), true);
      dlg.setLocationRelativeTo(this.MainFrame);
      dlg.setVisible(true);
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.tl = iesRMI.getTPOFromRMI();
    }
  }

  private void item_removeTaskActionPerformed(ActionEvent evt) {
    RemoveTask dlg = new RemoveTask(this, true);
    dlg.setLocationRelativeTo(this.MainFrame);
    dlg.setVisible(true);
  }

  private void item_removeJobActionPerformed(ActionEvent evt)
  {
    Object[] options = { "Remove", "Cancel" };

    int n = JOptionPane.showOptionDialog(this, "If you want to remove this job permanently, Click the remove button.", "Remove Job", 0, 3, null, options, options[0]);

    if (n == 0)
    {
      try
      {
        removeJob();
      }
      catch (TransformerConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (TransformerException ex)
      {
        log.debug(ex);
      }
      catch (RemoteException ex)
      {
        log.info(ex);
        this.tl = iesRMI.getTPOFromRMI();
      }
    }
  }

  private void item_jobPropertyActionPerformed(ActionEvent evt)
  {
    try {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.datanode.getParent();
      NodeInfo task = (NodeInfo)node.getUserObject();
      try
      {
        openProperty(task.getLongId(), this.popnode.getLongId(), this.popnode.getIdentifier(), null);
      }
      catch (RemoteException ex)
      {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      }
      catch (ExecutionTerminationException ex)
      {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    catch (ParserConfigurationException ex)
    {
      log.debug(ex);
    }
    catch (IOException ex)
    {
      log.debug(ex);
    }
  }

  private void refreshMenuActionPerformed(ActionEvent evt)
  {
    try
    {
      initTree();
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.ol = iesRMI.getDOFromRMI();
      this.tl = iesRMI.getTPOFromRMI();
    }
    catch (ExecutionTerminationException ex)
    {
      log.info(ex);
    }
  }

  private void item_stopTaskActionPerformed(ActionEvent evt)
  {
    try {
      TaskState state = this.doo.currentState(this.popnode.getLongId());

      switch (100.$SwitchMap$dblab$core$task$TaskState[state.ordinal()])
        case 1:
          break;
        case 2:
          this.doo.stopTask(this.popnode.getLongId());
          break;
      }

      state = this.doo.currentState(this.popnode.getLongId());
      switchTaskToolBar(state);
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.ol = iesRMI.getDOFromRMI();
      this.tl = iesRMI.getTPOFromRMI();
      this.doo = iesRMI.getDeployerFromRMI();
    }
  }

  private void item_runTaskActionPerformed(ActionEvent evt)
  {
    try {
      TaskState state = this.doo.currentState(this.popnode.getLongId());

      System.out.println("State : " + this.doo);

      switch (100.$SwitchMap$dblab$core$task$TaskState[state.ordinal()])
      {
      case 1:
        this.doo.freezeTask(this.popnode.getLongId());
        break;
      case 2:
        this.doo.rerrunTask(this.popnode.getLongId());
        break;
      default:
        this.doo.runTask(this.popnode.getLongId());
        addRunTaskId(this.popnode.getLongId());
      }

      state = this.doo.currentState(this.popnode.getLongId());
      switchTaskToolBar(state);
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.tl = iesRMI.getTPOFromRMI();
      this.doo = iesRMI.getDeployerFromRMI();
    }
  }

  private void jTextField2ActionPerformed(ActionEvent evt)
  {
  }

  private void jButton11ActionPerformed(ActionEvent evt)
  {
  }

  private void jButton12ActionPerformed(ActionEvent evt) {
    this.PipeSelectDlg.setVisible(false);
  }

  private void jButton10ActionPerformed(ActionEvent evt) {
    this.PipeSelectDlg.setLocationRelativeTo(this.MainFrame);
    this.PipeSelectDlg.setSize(new Dimension(420, 260));
    this.PipeSelectDlg.setVisible(true);
  }

  private void item_taskPropertyActionPerformed(ActionEvent evt) {
    openTaskProperty();
  }

  private void item_displayActionPerformed(ActionEvent evt)
  {
    try
    {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.datanode.getParent();
      NodeInfo task = (NodeInfo)node.getUserObject();
      try
      {
        openProperty(task.getLongId(), this.popnode.getLongId(), this.popnode.getIdentifier(), "Display");
      }
      catch (RemoteException ex)
      {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      }
      catch (ExecutionTerminationException ex)
      {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    catch (ParserConfigurationException ex)
    {
      log.debug(ex);
    }
    catch (IOException ex)
    {
      log.debug(ex);
    }
  }

  private void item_parameterActionPerformed(ActionEvent evt)
  {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.datanode.getParent();
    NodeInfo task = (NodeInfo)node.getUserObject();
    try
    {
      try
      {
        openProperty(task.getLongId(), this.popnode.getLongId(), this.popnode.getIdentifier(), "Parameter");
      }
      catch (RemoteException ex)
      {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      }
      catch (ExecutionTerminationException ex)
      {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    catch (ParserConfigurationException ex)
    {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
    catch (IOException ex)
    {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void jMenuItem3ActionPerformed(ActionEvent evt) {
    initDDLCreateDialog(this.popnode.getName());
    this.create_mode = true;
    this.old_ddl = this.current_ddl;
    this.current_ddl = new DataDefinition();
    this.DDLCreateDialog.setLocationRelativeTo(this.MainFrame);
    this.DDLCreateDialog.setSize(new Dimension(600, 410));
    this.DDLCreateDialog.setVisible(true);
  }

  private void item_insertQuery2ActionPerformed(ActionEvent evt) {
    Vector ddls = new Vector();
    try
    {
      IEDescriptor descriptor = null;

      Registry registry = LocateRegistry.getRegistry(Servers.SERVER_IP);
      descriptor = (IEDescriptor)registry.lookup(Servers.DESCRIPTOR_NAME);

      ddls = descriptor.getDataDefinitionList(this.tl.getOwnerByTaskId(this.popnode.getLongId()));
    }
    catch (NotBoundException ex)
    {
      log.info(ex);
    }
    catch (AccessException ex)
    {
      log.info(ex);
    }
    catch (RemoteException ex)
    {
      log.info(ex);
    }

    InsertQuery dialog = new InsertQuery(this, true, ddls);
    dialog.setLocationRelativeTo(this.MainFrame);
    dialog.setVisible(true);
  }

  private void item_planGenerationActionPerformed(ActionEvent evt) {
    PlanGeneration dialog = new PlanGeneration(this, true, this.popnode.getName());
    dialog.setLocationRelativeTo(this.MainFrame);
    dialog.setVisible(true);
    try
    {
      long taskId = getNodeInfo().getLongId();
      this.tl.initDocument(taskId, "config/task.xml");
      this.tl.rebuildTaskXmlFile("config/task.xml");

      NodeInfo[] jobs = this.tl.getJobList(getNodeInfo().getLongId());
      for (int i = 0; i < jobs.length; i++)
      {
        NodeInfo node = new NodeInfo(jobs[i].getName(), "job", jobs[i].getLongId());
        DefaultMutableTreeNode localDefaultMutableTreeNode1 = insetJobToTree(node);
      }

      for (int i = 0; i < jobs.length; i++)
      {
        NodeInfo node = new NodeInfo(jobs[i].getName(), "job", jobs[i].getLongId());
        DefaultMutableTreeNode newJob = new DefaultMutableTreeNode(node);
        Point point = this.tl.getJobLocation(taskId, jobs[i].getLongId());
        this.job_viewer.insertButton(i, new Job(node
          .getLongId(), jobs[i].getIdentifier(), jobs[i].getName(), false), newJob,
          (int)point
          .getX(), (int)point.getY());
      }
    }
    catch (ParserConfigurationException ex)
    {
      log.info(ex);
    }
    catch (TransformerConfigurationException ex)
    {
      log.info(ex);
    }
    catch (TransformerException ex)
    {
      log.info(ex);
    }
    catch (RemoteException ex)
    {
      log.info(ex);
    }
    catch (IOException ex)
    {
      log.info(ex);
    }
  }

  private void item_runTask1item_runTaskActionPerformed(ActionEvent evt)
  {
    try
    {
      TaskState state = this.doo.currentState(this.popnode.getLongId());

      System.out.println("State : " + this.doo);

      switch (100.$SwitchMap$dblab$core$task$TaskState[state.ordinal()])
      {
      case 1:
        this.doo.freezeTask(this.popnode.getLongId());
        break;
      case 2:
        this.doo.rerrunTask(this.popnode.getLongId());
        break;
      default:
        this.doo.runTask(this.popnode.getLongId());
        addRunTaskId(this.popnode.getLongId());
      }

      state = this.doo.currentState(this.popnode.getLongId());
      switchTaskToolBar(state);
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.tl = iesRMI.getTPOFromRMI();
      this.doo = iesRMI.getDeployerFromRMI();
    }
  }

  private void item_stopTask1item_stopTaskActionPerformed(ActionEvent evt)
  {
    try {
      TaskState state = this.doo.currentState(this.popnode.getLongId());

      switch (100.$SwitchMap$dblab$core$task$TaskState[state.ordinal()])
      {
      case 1:
      case 2:
        this.doo.stopTask(this.popnode.getLongId());
        break;
      }

      state = this.doo.currentState(this.popnode.getLongId());
      switchTaskToolBar(state);
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.ol = iesRMI.getDOFromRMI();
      this.tl = iesRMI.getTPOFromRMI();
      this.doo = iesRMI.getDeployerFromRMI();
    }
  }

  private void item_taskProperty1item_taskPropertyActionPerformed(ActionEvent evt) {
    openTaskProperty();
  }

  private void item_removeTask1item_removeTaskActionPerformed(ActionEvent evt) {
    RemoveTask dlg = new RemoveTask(this, true);
    dlg.setLocationRelativeTo(this.MainFrame);
    dlg.setVisible(true);
  }

  private void item_preloadTaskActionPerformed(ActionEvent evt)
  {
    try {
      String flag = this.tl.getTaskPreloadFlag(this.popnode.getLongId());
      if (flag.equals("false")) {
        this.tl.setTaskPreloadFlag(this.popnode.getLongId(), "true");
        this.item_preloadTask.setText("Preload Disable");
      }
      else if (flag.equals("true")) {
        this.tl.setTaskPreloadFlag(this.popnode.getLongId(), "false");
        this.item_preloadTask.setText("Preload Enable");
      }
      else {
        log.info("Preload flag is wrong.");
      }
      this.tl.rebuildTaskXmlFile("config/task.xml");
    }
    catch (RemoteException ex)
    {
      log.info(ex);
    }
    catch (TransformerException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void TaskPauseTBtnActionPerformed(ActionEvent evt)
  {
    try
    {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.NaviTree.getLastSelectedPathComponent();
      NodeInfo type = (NodeInfo)node.getUserObject();

      if (type.getType().equals("job"))
      {
        node = (DefaultMutableTreeNode)node.getParent();
        type = (NodeInfo)node.getUserObject();
      }

      this.doo.freezeTask(type.getLongId());

      TaskState state = this.doo.currentState(type.getLongId());
      switchTaskToolBar(state);
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.tl = iesRMI.getTPOFromRMI();
      this.doo = iesRMI.getDeployerFromRMI();
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
  }

  private void TaskRerunTBtnActionPerformed(ActionEvent evt)
  {
    try {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.NaviTree.getLastSelectedPathComponent();
      NodeInfo type = (NodeInfo)node.getUserObject();

      if (type.getType().equals("job"))
      {
        node = (DefaultMutableTreeNode)node.getParent();
        type = (NodeInfo)node.getUserObject();
      }

      this.doo.rerrunTask(type.getLongId());

      TaskState state = this.doo.currentState(type.getLongId());
      switchTaskToolBar(state);
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.tl = iesRMI.getTPOFromRMI();
      this.doo = iesRMI.getDeployerFromRMI();
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
  }

  private void TaskStopTBtnActionPerformed(ActionEvent evt)
  {
    try
    {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.NaviTree.getLastSelectedPathComponent();
      NodeInfo type = (NodeInfo)node.getUserObject();

      if (type.getType().equals("job"))
      {
        node = (DefaultMutableTreeNode)node.getParent();
        type = (NodeInfo)node.getUserObject();
      }

      this.doo.stopTask(type.getLongId());

      TaskState state = this.doo.currentState(type.getLongId());
      switchTaskToolBar(state);
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.tl = iesRMI.getTPOFromRMI();
      this.doo = iesRMI.getDeployerFromRMI();
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
  }

  private void jButton9ActionPerformed(ActionEvent evt)
  {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.NaviTree.getLastSelectedPathComponent();

    if (node == null)
    {
      warnMsgDlg(this, "You must select tree's task user, task, or job node first.");
      return;
    }

    NodeInfo type = (NodeInfo)node.getUserObject();

    if (type.getType().equals("taskuser"))
    {
      this.popnode = type;
      this.datanode = node;
    }
    else if (type.getType().equals("task"))
    {
      node = (DefaultMutableTreeNode)node.getParent();
      type = (NodeInfo)node.getUserObject();
      this.popnode = type;
      this.datanode = node;
    }
    else if (type.getType().equals("job"))
    {
      node = (DefaultMutableTreeNode)node.getParent().getParent();
      type = (NodeInfo)node.getUserObject();
      this.popnode = type;
      this.datanode = node;
    }
    else
    {
      warnMsgDlg(this, "You must select tree's task user, task, or job node first.");
      return;
    }

    TaskInsertion dlg = new TaskInsertion(this, true);
    dlg.setLocationRelativeTo(this.MainFrame);
    dlg.setVisible(true);
  }

  private void jButton2ActionPerformed(ActionEvent evt)
  {
    DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.NaviTree.getLastSelectedPathComponent();

    if (node == null)
    {
      warnMsgDlg(this, "You must select tree's ddl owner or ddl node first.");
      return;
    }

    NodeInfo type = (NodeInfo)node.getUserObject();

    if (type.getType().equals("user"))
    {
      this.popnode = type;
      this.datanode = node;
      initDDLCreateDialog(type.getName());
    }
    else if (type.getType().equals("ddl"))
    {
      node = (DefaultMutableTreeNode)node.getParent();
      type = (NodeInfo)node.getUserObject();
      this.popnode = type;
      this.datanode = node;
      initDDLCreateDialog(type.getName());
    }
    else
    {
      warnMsgDlg(this, "You must select tree's ddl owner or ddl node first.");
      return;
    }

    this.create_mode = true;
    this.old_ddl = this.current_ddl;
    this.current_ddl = new DataDefinition();
    this.DDLCreateDialog.setLocationRelativeTo(this.MainFrame);
    this.DDLCreateDialog.setSize(new Dimension(600, 410));
    this.DDLCreateDialog.setVisible(true);
  }

  private void TaskRunTBtnActionPerformed(ActionEvent evt)
  {
    try
    {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.NaviTree.getLastSelectedPathComponent();
      NodeInfo type = (NodeInfo)node.getUserObject();

      if (type.getType().equals("job"))
      {
        node = (DefaultMutableTreeNode)node.getParent();
        type = (NodeInfo)node.getUserObject();
      }

      this.doo.runTask(type.getLongId());
      addRunTaskId(type.getLongId());

      TaskState state = this.doo.currentState(type.getLongId());
      switchTaskToolBar(state);
      new TaskExecutionMonitor(type.getLongId()).start();
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.tl = iesRMI.getTPOFromRMI();
      this.doo = iesRMI.getDeployerFromRMI();
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
  }

  private void item_insert_IP_TableActionPerformed(ActionEvent evt)
  {
    InsertIpTableTask ipTableTask = new InsertIpTableTask(this, true);
    ipTableTask.setLocationRelativeTo(this.MainFrame);
    ipTableTask.setVisible(true);
  }

  private void applyButtonActionPerformed(ActionEvent evt)
  {
    if ((this.nameTextField.getText().equals("")) || (this.ipAddressTextField.getText().equals("")))
    {
      JOptionPane.showMessageDialog(this, "Name,IP-Address Field can't be null value");
      return;
    }
    try
    {
      int i = Integer.parseInt(this.heartbeatPortTextField.getText());
    }
    catch (NumberFormatException ex)
    {
      JOptionPane.showMessageDialog(this, "Heartbeat Port must be Integer.");
      return;
    }

    try
    {
      this.cur_task.setName(this.nameTextField.getText());
      this.cur_task.setIpAddress(this.ipAddressTextField.getText());
      this.cur_task.setRmiSuffix(this.rmiSuffixTextField.getText());
      this.cur_task.setHeartbeatPort(Integer.parseInt(this.heartbeatPortTextField.getText()));
      this.cur_task.setInputPorts(this.inputPortTextField.getText());
      this.cur_task.setWorker(this.WorkerComboBox.getItemAt(this.WorkerComboBox.getSelectedIndex()).toString());
      try
      {
        this.ipTableTasks.setTaskInfoById(this.cur_task.getId(), this.cur_task);
      } catch (RemoteException ex) {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      }
      try {
        this.ipTableTasks.rebuildipTableXmlFile();
      } catch (RemoteException ex) {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      } catch (TransformerConfigurationException ex) {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      } catch (TransformerException ex) {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      }

    }
    catch (NumberFormatException ex)
    {
      JOptionPane.showMessageDialog(this, "Only numbers in Command Port Number Field are possible.");
    }
  }

  private void jButton14ActionPerformed(ActionEvent evt)
  {
    try {
      this.ipTableTasks.startHeartbeatChecker();
    } catch (RemoteException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void jButton13ActionPerformed(ActionEvent evt)
  {
    try {
      this.ipTableTasks.closeHeartbeatChecker();
    } catch (RemoteException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void jButton15ActionPerformed(ActionEvent evt)
  {
    try {
      this.ipTableTasks.setTimerPeriod(Integer.valueOf(this.TimerPeriodTextField.getText()).intValue());
    } catch (RemoteException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void IP_TableRefreshButtonActionPerformed(ActionEvent evt)
  {
    DefaultTableModel dModel = (DefaultTableModel)this.ipTableTaskListTable.getModel();
    try
    {
      ipTableTaskInfo[] tasks = this.ipTableTasks.getAllTaskInfos();
      dModel.setRowCount(tasks.length);
      for (int i = 0; i < tasks.length; i++)
      {
        dModel.setValueAt(tasks[i].getName(), i, 0);
        dModel.setValueAt(tasks[i].getIpAddress(), i, 1);
        dModel.setValueAt(tasks[i].getRmiSuffix(), i, 2);
        dModel.setValueAt(Integer.valueOf(tasks[i].getHeartbeatPort()), i, 3);
        dModel.setValueAt(tasks[i].getInputPorts(), i, 4);
        dModel.setValueAt(tasks[i].getWorker(), i, 5);
        dModel.setValueAt(Boolean.valueOf(tasks[i].getStatus()), i, 6);
      }
      this.cur_view = this.ipTableTaskListPanel;
    } catch (IOException localIOException) {
    }
    this.Controllers.add(this.cur_view, "Center");
    this.cur_view.setVisible(true);
  }

  private void WorkerComboBoxActionPerformed(ActionEvent evt)
  {
  }

  private void MapReduceRunBtnActionPerformed(ActionEvent evt)
  {
    try
    {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.NaviTree.getLastSelectedPathComponent();
      NodeInfo type = (NodeInfo)node.getUserObject();

      if (type.getType().equals("job")) {
        node = (DefaultMutableTreeNode)node.getParent();
        type = (NodeInfo)node.getUserObject();
      }

      this.doo.runTask(type.getLongId());
      addRunTaskId(type.getLongId());
      TaskState state = this.doo.currentState(type.getLongId());
      switchTaskToolBar(state);
      switchMapReduceTaskToolBar(state);
      new TaskExecutionMonitor(type.getLongId()).start();

      ArrayList taskIDs = new ArrayList();
      taskIDs = this.tl.getMapReduceMasterInfo(type.getLongId(), "reducer-taskids", "reducer-taskid", "taskid");
      if ((taskIDs != null) && (taskIDs.size() > 0)) {
        for (int i = 0; i < RMI_Controller.reducerDeployers.length; i++) {
          RMI_Controller.reducerDeployers[i].runTask(Long.valueOf((String)taskIDs.get(i)).longValue());
          if (RMI_Controller.reducerDeployers[i].equals(this.doo)) {
            addRunTaskId(Long.valueOf((String)taskIDs.get(i)).longValue());
          }
          TaskState s = RMI_Controller.reducerDeployers[i].currentState(Long.valueOf((String)taskIDs.get(i)).longValue());
          switchTaskToolBar(s);
          new TaskExecutionMonitor(Long.valueOf((String)taskIDs.get(i)).longValue()).start();
        }
      }
      taskIDs = this.tl.getMapReduceMasterInfo(type.getLongId(), "mapper-taskids", "mapper-taskid", "taskid");
      if ((taskIDs != null) && (taskIDs.size() > 0)) {
        for (int i = 0; i < RMI_Controller.mapperDeployers.length; i++) {
          RMI_Controller.mapperDeployers[i].runTask(Long.valueOf((String)taskIDs.get(i)).longValue());
          if (RMI_Controller.mapperDeployers[i].equals(this.doo)) {
            addRunTaskId(Long.valueOf((String)taskIDs.get(i)).longValue());
          }
          TaskState s = RMI_Controller.mapperDeployers[i].currentState(Long.valueOf((String)taskIDs.get(i)).longValue());
          switchTaskToolBar(s);
          new TaskExecutionMonitor(Long.valueOf((String)taskIDs.get(i)).longValue()).start();
        }
      }
      taskIDs = this.tl.getMapReduceMasterInfo(type.getLongId(), "splitter-taskids", "splitter-taskid", "taskid");
      if ((taskIDs != null) && (taskIDs.size() > 0))
        for (int i = 0; i < RMI_Controller.splitterDeployers.length; i++) {
          RMI_Controller.splitterDeployers[i].runTask(Long.valueOf((String)taskIDs.get(i)).longValue());
          if (RMI_Controller.splitterDeployers[i].equals(this.doo)) {
            addRunTaskId(Long.valueOf((String)taskIDs.get(i)).longValue());
          }
          TaskState s = RMI_Controller.splitterDeployers[i].currentState(Long.valueOf((String)taskIDs.get(i)).longValue());
          switchTaskToolBar(s);
          new TaskExecutionMonitor(Long.valueOf((String)taskIDs.get(i)).longValue()).start();
        }
    }
    catch (RemoteException ex) {
      log.info(ex);
      this.tl = iesRMI.getTPOFromRMI();
      this.doo = iesRMI.getDeployerFromRMI();
    } catch (ExecutionTerminationException ex) {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
  }

  private void MapReduceStopBtnActionPerformed(ActionEvent evt)
  {
    try
    {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.NaviTree.getLastSelectedPathComponent();
      NodeInfo type = (NodeInfo)node.getUserObject();

      if (type.getType().equals("job"))
      {
        node = (DefaultMutableTreeNode)node.getParent();
        type = (NodeInfo)node.getUserObject();
      }

      this.doo.stopTask(type.getLongId());

      TaskState state = this.doo.currentState(type.getLongId());
      switchTaskToolBar(state);
      switchMapReduceTaskToolBar(state);

      ArrayList taskIDs = new ArrayList();
      taskIDs = this.tl.getMapReduceMasterInfo(type.getLongId(), "reducer-taskids", "reducer-taskid", "taskid");
      if ((taskIDs != null) && (taskIDs.size() > 0)) {
        for (int i = 0; i < RMI_Controller.reducerDeployers.length; i++) {
          RMI_Controller.reducerDeployers[i].stopTask(Long.valueOf((String)taskIDs.get(i)).longValue());
          TaskState s = RMI_Controller.reducerDeployers[i].currentState(Long.valueOf((String)taskIDs.get(i)).longValue());
          switchTaskToolBar(s);
        }
      }
      taskIDs = this.tl.getMapReduceMasterInfo(type.getLongId(), "mapper-taskids", "mapper-taskid", "taskid");
      if ((taskIDs != null) && (taskIDs.size() > 0)) {
        for (int i = 0; i < RMI_Controller.mapperDeployers.length; i++) {
          RMI_Controller.mapperDeployers[i].stopTask(Long.valueOf((String)taskIDs.get(i)).longValue());
          TaskState s = RMI_Controller.mapperDeployers[i].currentState(Long.valueOf((String)taskIDs.get(i)).longValue());
          switchTaskToolBar(s);
        }
      }
      taskIDs = this.tl.getMapReduceMasterInfo(type.getLongId(), "splitter-taskids", "splitter-taskid", "taskid");
      if ((taskIDs != null) && (taskIDs.size() > 0)) {
        for (int i = 0; i < RMI_Controller.splitterDeployers.length; i++) {
          RMI_Controller.splitterDeployers[i].stopTask(Long.valueOf((String)taskIDs.get(i)).longValue());
          TaskState s = RMI_Controller.splitterDeployers[i].currentState(Long.valueOf((String)taskIDs.get(i)).longValue());
          switchTaskToolBar(s);
        }
      }
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.tl = iesRMI.getTPOFromRMI();
      this.doo = iesRMI.getDeployerFromRMI();
    }
    catch (ExecutionTerminationException ex)
    {
      log.debug(ex);
      warnMsgDlg(this, ex.getMessage());
    }
  }

  private void systemMonitorButtonActionPerformed(ActionEvent evt)
  {
    if (!this.onSystemMonitorDialog) {
      startSystemInfoTimer();
      this.systemMonitorDialog.setVisible(true);
      this.onSystemMonitorDialog = true;
    }
  }

  private void systemMonitorDialogWindowClosed(WindowEvent evt)
  {
    try {
      closeSystemInfoTimer();
    } catch (RemoteException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
    this.onSystemMonitorDialog = false;
  }

  public void setStartExportTimer(long cur_taskId) {
    startExportSystemInfoTimer(cur_taskId);
    this.exportTaskIDList.put(Long.valueOf(cur_taskId), Boolean.valueOf(true));
    this.exportButton.setText("Stop Export");
  }

  public void setExportTimer(long taskID) {
    if (this.exportTaskIDList.isEmpty()) {
      setStartExportTimer(taskID);
    }
    else if (this.exportTaskIDList.containsKey(Long.valueOf(taskID))) {
      boolean exportFlag = ((Boolean)this.exportTaskIDList.get(Long.valueOf(taskID))).booleanValue();
      if (exportFlag) {
        closeExportSystemInfoTimer(taskID);
        this.exportTaskIDList.put(Long.valueOf(taskID), Boolean.valueOf(false));
        this.exportButton.setText("Start Export");
      }
      else {
        setStartExportTimer(taskID);
      }
    }
    else {
      setStartExportTimer(taskID);
    }
  }

  private void exportButtonActionPerformed(ActionEvent evt)
  {
    setExportTimer(this.cur_taskId);
  }

  private JFreeChart createChart(XYDataset dataset1, XYDataset dataset2, XYDataset[] datasetArray)
  {
    JFreeChart result = ChartFactory.createTimeSeriesChart("System Information Monitor", "Time", "Usage(%)", dataset1, true, true, false);

    XYPlot plot = result.getXYPlot();
    plot.setBackgroundPaint(Color.LIGHT_GRAY);
    ValueAxis axis = plot.getDomainAxis();
    axis.setAutoRange(true);
    axis.setFixedAutoRange(60000.0D);
    axis = plot.getRangeAxis();
    axis.setRange(0.0D, 100.0D);

    plot.setDataset(1, dataset2);
    plot.setRenderer(1, new DefaultXYItemRenderer());
    plot.setRangeAxis(1, axis);
    plot.mapDatasetToRangeAxis(1, 1);
    if (datasetArray != null) {
      NumberAxis rangeAxis2 = new NumberAxis("Input Rate(tuple/sec)");
      rangeAxis2.setAutoRangeIncludesZero(false);
      rangeAxis2.setRange(0.0D, 2000.0D);
      for (int i = 2; i < datasetArray.length + 2; i++) {
        plot.setDataset(i, datasetArray[(i - 2)]);
        plot.setRenderer(i, new DefaultXYItemRenderer());
        plot.setRangeAxis(rangeAxis2);
        plot.mapDatasetToRangeAxis(i, 0);
      }
    }
    return result;
  }

  public void actionGraph(double cpuUsage, double memUsage, long[] inputRates) {
    this.series1.add(new Millisecond(), cpuUsage);
    this.series2.add(new Millisecond(), memUsage);
    if ((inputRates != null) && (inputRates.length == this.seriesArray.length)) {
      for (int i = 0; i < inputRates.length; i++)
        this.seriesArray[i].add(new Millisecond(), inputRates[i]);
    }
    else if ((inputRates != null) && (inputRates.length != this.seriesArray.length))
    {
      this.systemMonitorViewDialog.setVisible(false);
      this.onViewGraph = false;
      setSystemMonitorDialog();
    }
  }

  public void setSystemMonitorDialog() {
    if (!this.onViewGraph) {
      this.series1 = new TimeSeries("CPU Usage", Millisecond.class);
      this.series2 = new TimeSeries("Memory Usage", Millisecond.class);
      this.seriesArray = new TimeSeries[this.baseCount];
      for (int i = 0; i < this.baseCount; i++) {
        this.seriesArray[i] = new TimeSeries("Input Rate_" + i, Millisecond.class);
      }
      TimeSeriesCollection dataset1 = new TimeSeriesCollection(this.series1);
      TimeSeriesCollection dataset2 = new TimeSeriesCollection(this.series2);
      TimeSeriesCollection[] datasetArray = new TimeSeriesCollection[this.baseCount];
      for (int i = 0; i < this.baseCount; i++) {
        datasetArray[i] = new TimeSeriesCollection(this.seriesArray[i]);
      }
      JFreeChart chart = createChart(dataset1, dataset2, datasetArray);

      ChartPanel chartPanel = new ChartPanel(chart);
      JPanel content = new JPanel(new BorderLayout());
      content.add(chartPanel);
      chartPanel.setPreferredSize(new Dimension(500, 300));
      this.systemMonitorViewDialog.setContentPane(content);
      this.systemMonitorViewDialog.setVisible(true);
      this.onViewGraph = true;
    }
  }

  private void viewSystemMonitorButtonActionPerformed(ActionEvent evt)
  {
    setSystemMonitorDialog();
  }

  private void systemMonitorViewDialogWindowClosed(WindowEvent evt)
  {
    this.onViewGraph = false;
  }

  private void mapReduceSIMComboBoxActionPerformed(ActionEvent evt)
  {
    String str = (String)this.mapReduceSIMComboBox.getSelectedItem();
    if (str == null)
      return;
    if (this.mapReduceSIMComboBox.getSelectedIndex() == 0) {
      this.mapReduceRMI_InfoFlag = false;
      this.exportButton.setEnabled(true);
      return;
    }
    this.exportButton.setEnabled(false);

    int i = 0;
    String ip = null; String suffix = null; String taskID = null;
    for (String s : str.split(" ")) {
      if (i == 1)
        ip = s;
      else if (i == 2)
        suffix = s;
      else if (i == 3)
        taskID = s;
      i++;
    }
    this.selectedTaskID = Integer.valueOf(taskID).intValue();
    try {
      Registry registry = LocateRegistry.getRegistry(ip);
      this.mapReduceDO = ((DataOperation)registry.lookup("descriptor" + suffix));

      registry = LocateRegistry.getRegistry(ip);
      this.mapReducePO = ((PropertyOperation)registry.lookup("descriptor" + suffix));

      registry = LocateRegistry.getRegistry(ip);
      this.mapReduceIED = ((IEDeployer)registry.lookup("deployer" + suffix));
    } catch (RemoteException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    } catch (NotBoundException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
    this.mapReduceRMI_InfoFlag = true;
  }

  private void removeIPTableButtonActionPerformed(ActionEvent evt)
  {
    try
    {
      setPopupNodeByIPTableId(this.popnode.getLongId());
      this.ipTableTasks.removeTaskInfo(this.popnode.getLongId());
      this.ipTableTasks.rebuildipTableXmlFile();
    } catch (RemoteException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    } catch (TransformerConfigurationException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    } catch (TransformerException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
    this.cur_view.setVisible(false);
    this.Controllers.remove(this.cur_view);
    this.cur_view = this.ipTableTaskListPanel;
    this.Controllers.add(this.cur_view, "Center");
    this.cur_view.setVisible(true);

    DefaultTreeModel NaviModel = (DefaultTreeModel)this.NaviTree.getModel();
    NaviModel.removeNodeFromParent(this.datanode);
  }

  private void item_copyTaskActionPerformed(ActionEvent evt)
  {
    CopyTaskAndDDL copyTask = new CopyTaskAndDDL(this, true, 0);
    copyTask.setLocationRelativeTo(this.MainFrame);
    copyTask.setVisible(true);
  }

  private void copyDDLMenuItemActionPerformed(ActionEvent evt)
  {
    CopyTaskAndDDL copyDDL = new CopyTaskAndDDL(this, true, 1);
    copyDDL.setLocationRelativeTo(this.MainFrame);
    copyDDL.setVisible(true);
  }

  private void item_makeTemplateActionPerformed(ActionEvent evt) {
    try {
      NodeInfo[] taskList = this.tl.getTaskList(this.loginedName);
      SelectTaskTemplate selectTask = new SelectTaskTemplate(this, taskList, true);
      selectTask.setLocationRelativeTo(this.MainFrame);
      selectTask.setVisible(true);
    } catch (RemoteException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void item_hiddenActionPerformed(ActionEvent evt) {
    try {
      this.tl.setTaskHidden(this.popnode.getLongId());
      this.tl.rebuildTaskXmlFile("config/task.xml");
    } catch (RemoteException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    } catch (TransformerException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public ArrayList<TemplateSource> getTaskData(NodeInfo[] taskList)
  {
    ArrayList result = new ArrayList();
    try {
      for (int index = 0; index < taskList.length; index++) {
        this.tl.initDocument(taskList[index].getLongId(), "config/task.xml");
        Element task = this.tl.getElementByTaskID(taskList[index].getLongId());
        Element taskName = (Element)task.getElementsByTagName("name").item(0);
        String nameParam = taskName.getTextContent();
        TemplateSource taskNameSource = new TemplateSource(taskName, null, taskList[index], nameParam, 0);
        result.add(taskNameSource);

        Element taskDesc = (Element)task.getElementsByTagName("description").item(0);
        String descParam = taskDesc.getTextContent();
        TemplateSource taskDescSource = new TemplateSource(taskDesc, null, taskList[index], descParam, 0);
        result.add(taskDescSource);

        NodeList jobs = task.getElementsByTagName("job");
        NodeInfo[] nodeInfos = this.tl.getJobList(taskList[index].getLongId());
        for (int i = 0; i < jobs.getLength(); i++) {
          Element job = (Element)jobs.item(i);
          int identifier = Integer.parseInt(job.getAttribute("identifier"));
          NodeInfo nodeInfo = null;
          for (int j = 0; j < nodeInfos.length; j++) {
            if (job.getAttribute("id").equals(Long.toString(nodeInfos[i].getLongId()))) {
              nodeInfo = nodeInfos[i];
            }

          }

          if (identifier == 2) {
            Element condition = (Element)job.getElementsByTagName("condition").item(0);
            if (condition == null) {
              continue;
            }
            Vector attr = JobMappingReaderIE.newinstnace().getAttribute(nodeInfo.getLongId(), this.tl, this.ol);
            String[] condString = condition.getTextContent().split("%AND%|%OR%");
            for (int j = 0; j < condString.length; j++) {
              ArrayList list = new ArrayList();
              Matcher matcher = Pattern.compile("\\d+").matcher(condString[j].trim());
              while (matcher.find()) {
                list.add(matcher.group().trim());
              }
              String attrName = ((Attribute)attr.get(Integer.parseInt((String)list.get(0)) - 1)).getName();
              String conditionString = attrName + condString[j].replace("%", "").trim().substring(((String)list.get(0)).length());
              TemplateSource source = new TemplateSource(condition, nodeInfo, taskList[index], conditionString, j);
              result.add(source);
            }
          } else if (identifier == 14)
          {
            Element windowSize = (Element)job.getElementsByTagName("window-size").item(0);
            if (windowSize == null) {
              continue;
            }
            String windowSizeStr = taskName.getTextContent() + ".window-size";
            TemplateSource windowSource = new TemplateSource(windowSize, nodeInfo, taskList[index], windowSizeStr, 0);
            result.add(windowSource);

            NodeList having = job.getElementsByTagName("having");
            if (having.getLength() == 0) {
              continue;
            }
            String[] query = job.getElementsByTagName("query-string").item(0).getTextContent().split("Having")[1].trim().split("AND|OR");
            for (int j = 0; j < having.getLength(); j++) {
              TemplateSource source = new TemplateSource((Element)having.item(j), nodeInfo, taskList[index], query[j], j);
              result.add(source);
            }
          } else if ((identifier == 35) || (identifier == 1)) {
            Element pipeName = (Element)job.getElementsByTagName("pipe-name").item(0);
            if (pipeName == null) {
              continue;
            }
            String PipeString = pipeName.getTextContent();
            TemplateSource source = new TemplateSource(pipeName, nodeInfo, taskList[index], PipeString, 0);
            result.add(source);
          }

          NodeList exports = job.getElementsByTagName("export");
          for (int j = 0; j < exports.getLength(); j++) {
            Element export = (Element)exports.item(j);
            String exportStr = export.getAttribute("destination-path");
            TemplateSource exportSource = new TemplateSource(export, nodeInfo, taskList[index], exportStr, j);
            result.add(exportSource);
          }
        }
      }
    }
    catch (RemoteException e) {
      log.info(e);
      this.tl = iesRMI.getTPOFromRMI();
    } catch (IOException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ParserConfigurationException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
    return result;
  }

  public void TemplateMake(ArrayList<TemplateSource> source, NodeInfo[] taskList) {
    ArrayList ddlElement = new ArrayList();
    ArrayList taskElement = new ArrayList();
    ArrayList ddllist = new ArrayList();
    try {
      this.templateOper.initTODocument("config/template.xml");
      for (int i = 0; i < taskList.length; i++) {
        Element task = this.tl.getElementByTaskID(taskList[i].getLongId());
        String[] ddlId = this.ol.getValuesByTagName(task, "data-definition-id");
        for (int j = 0; j < ddlId.length; j++) {
          if (!ddllist.contains(Integer.valueOf(Integer.parseInt(ddlId[j])))) {
            ddlElement.add(this.ol.getElementByDdlID(Long.parseLong(ddlId[j])));
            ddllist.add(Integer.valueOf(Integer.parseInt(ddlId[j])));
          }
        }
        taskElement.add(task);
      }
      this.templateOper.setDDLElement(ddlElement);
      this.templateOper.setTaskElement(taskElement, source);
      this.templateOper.rebuildTemplateXmlFile("config/template.xml");
    } catch (RemoteException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    } catch (ParserConfigurationException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    } catch (TransformerException ex) {
      java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void openTaskProperty()
  {
    try
    {
      Task pro = new Task(this, true, this.popnode.getLongId(), this.ol, this.tl, this.doo);

      pro.initProperties();
      pro.ViewDialog(this.MainFrame);
    }
    catch (RemoteException ex)
    {
      log.info(ex);
      this.tl = iesRMI.getTPOFromRMI();
    }
  }

  public void openProperty(long taskId, long JobId, int Iden, String name) throws ParserConfigurationException, IOException, ExecutionTerminationException
  {
    this.tl.initDocument(taskId, "config/task.xml");

    if (Iden == 1)
    {
      try
      {
        Basestream pro = new Basestream(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 2)
    {
      try
      {
        Selection pro = new Selection(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if ((Iden == 3) || (Iden == 23))
    {
      try
      {
        FrequentItemsets pro = new FrequentItemsets(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 4)
    {
      try
      {
        SequencePatterns pro = new SequencePatterns(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 5)
    {
      try
      {
        Association pro = new Association(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 6)
    {
      try
      {
        Transaction pro = new Transaction(this, true, this.ol, Iden);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 7)
    {
      try
      {
        Projection pro = new Projection(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 8)
    {
      try
      {
        Split pro = new Split(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
      catch (NotBoundException nbe)
      {
        log.debug(nbe);
      }
    }
    else if ((Iden == 9) || (Iden == 10) || (Iden == 101))
    {
      try
      {
        Clustering pro = new Clustering(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 20)
    {
      try
      {
        Olap pro = new Olap(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 11)
    {
      try
      {
        Selection_ms pro = new Selection_ms(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 12)
    {
      try
      {
        if (this.tl.getTaskType(taskId).equals("DataStreamQueryProcessing"))
        {
          BinaryJoin_ms pro = new BinaryJoin_ms(this, true, this.ol);
          pro.settl(this.tl, this.doo);
          pro.initProperties(taskId, JobId);
          if (name != null)
          {
            pro.setTreeNode(name);
          }
          pro.ViewDialog(this.MainFrame);
        }
        else
        {
          BinaryJoin_mm pro = new BinaryJoin_mm(this, true, this.ol);
          pro.settl(this.tl, this.doo);
          pro.initProperties(taskId, JobId);
          if (name != null)
          {
            pro.setTreeNode(name);
          }
          pro.ViewDialog(this.MainFrame);
        }
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 13)
    {
      try
      {
        MJoin pro = new MJoin(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if ((Iden == 14) || (Iden == 21))
    {
      try
      {
        Aggregation pro = new Aggregation(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 15)
    {
      try
      {
        Final pro = new Final(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 16)
    {
      try
      {
        Analysis pro = new Analysis(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 22)
    {
      try
      {
        TopK pro = new TopK(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 24)
    {
      try
      {
        Union pro = new Union(this, true, this.ol, Iden);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 25)
    {
      try
      {
        ContextRule pro = new ContextRule(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);
        if (name != null)
        {
          pro.setTreeNode(name);
        }
        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 26)
    {
      try
      {
        ContextSequence pro = new ContextSequence(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);
        if (name != null)
        {
          pro.setTreeNode(name);
        }
        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }

    }
    else if (Iden == 27)
    {
      try
      {
        VelocityProp prop = new VelocityProp(this, true, this.ol);

        prop.settl(this.tl, this.doo);
        prop.initProperties(taskId, JobId);

        if (null != name) prop.setTreeNode(name);

        prop.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }

    }
    else if (Iden == 31)
    {
      try
      {
        HalfJoinPlus prop = new HalfJoinPlus(this, true, this.ol);

        prop.settl(this.tl, this.doo);
        prop.initProperties(taskId, JobId);

        if (null != name) prop.setTreeNode(name);

        prop.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }

    }
    else if (Iden == 29)
    {
      try
      {
        ExpressionProp prop = new ExpressionProp(this, true, this.ol);

        prop.settl(this.tl, this.doo);
        prop.initProperties(taskId, JobId);

        if (null != name) prop.setTreeNode(name);

        prop.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }

    }
    else if (Iden == 33)
    {
      try
      {
        Period prop = new Period(this, true, this.ol);

        prop.settl(this.tl, this.doo);
        prop.initProperties(taskId, JobId);

        if (null != name) prop.setTreeNode(name);

        prop.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }

    }
    else if (Iden == 34)
    {
      try
      {
        OneItemFrequent prop = new OneItemFrequent(this, true, this.ol);

        prop.settl(this.tl, this.doo);
        prop.initProperties(taskId, JobId);

        if (null != name) prop.setTreeNode(name);

        prop.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
    else if (Iden == 35)
    {
      try
      {
        SocketOutput pro = new SocketOutput(this, true, this.ol);
        pro.settl(this.tl, this.doo);
        pro.initProperties(taskId, JobId);

        if (name != null)
        {
          pro.setTreeNode(name);
        }

        pro.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }

    }
    else if (Iden == 36)
    {
      try
      {
        Context prop = new Context(this, true, this.ol);

        prop.settl(this.tl, this.doo);
        prop.initProperties(taskId, JobId);

        if (null != name) prop.setTreeNode(name);

        prop.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }

    }
    else if (Iden == 1001)
    {
      try
      {
        KospiIndex prop = new KospiIndex(this, true, this.ol);

        prop.settl(this.tl, this.doo);
        prop.initProperties(taskId, JobId);

        if (null != name) prop.setTreeNode(name);

        prop.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }

    }
    else if (Iden == 28)
    {
      try
      {
        MergeJob prop = new MergeJob(this, true, this.ol);

        prop.settl(this.tl, this.doo);
        prop.initProperties(taskId, JobId);

        if (null != name) prop.setTreeNode(name);

        prop.ViewDialog(this.MainFrame);
      }
      catch (ParserConfigurationException ex)
      {
        log.debug(ex);
      }
      catch (IOException ex)
      {
        log.debug(ex);
      }
    }
  }

  public DefaultMutableTreeNode addTask(NodeInfo task)
  {
    DefaultMutableTreeNode tasknode = new DefaultMutableTreeNode(new NodeInfo(task.getName(), "task", task.getLongId()));
    return tasknode;
  }

  public DefaultMutableTreeNode addJob(NodeInfo job)
  {
    DefaultMutableTreeNode jobnode = new DefaultMutableTreeNode(new NodeInfo(job.getName(), "job", job.getLongId(), job.getIdentifier()));
    return jobnode;
  }

  public void insertJob(int i, NodeInfo job) throws UnsupportedEncodingException, TransformerConfigurationException, TransformerException, RemoteException
  {
    NodeInfo node = new NodeInfo(job.getName(), "job", this.tl.addJob(this.popnode.getLongId(), job));

    DefaultMutableTreeNode newJob = insetJobToTree(node);

    this.tl.rebuildTaskXmlFile("config/task.xml");

    this.job_viewer.insertButton(i, new Job(node.getLongId(), job.getIdentifier(), job.getName(), false), newJob);
  }

  public DefaultMutableTreeNode insetJobToTree(NodeInfo node)
  {
    DefaultMutableTreeNode newJob = new DefaultMutableTreeNode(node);
    DefaultTreeModel NaviModel = (DefaultTreeModel)this.NaviTree.getModel();

    NaviModel.insertNodeInto(newJob, this.datanode, 0);

    return newJob;
  }

  public void insertTask(NodeInfo task) throws TransformerConfigurationException, TransformerException, RemoteException
  {
    NodeInfo node = new NodeInfo(task.getName(), "task", this.tl.addTask(this.popnode.getName(), task));

    DefaultMutableTreeNode newTask = new DefaultMutableTreeNode(node);
    DefaultTreeModel NaviModel = (DefaultTreeModel)this.NaviTree.getModel();

    this.tl.rebuildTaskXmlFile("config/task.xml");

    NaviModel.insertNodeInto(newTask, this.datanode, 0);
  }

  public boolean insertIPTableTask(ipTableTaskInfo task) throws TransformerConfigurationException, TransformerException, RemoteException {
    if (this.ipTableTasks.getTaskInfoByName(task.getName()) != null)
    {
      JOptionPane.showMessageDialog(this, "There is same named task.");
      return false;
    }
    NodeInfo node = new NodeInfo(task.getName(), "iptabletask");
    DefaultMutableTreeNode newTask = new DefaultMutableTreeNode(node);
    DefaultTreeModel NaviModel = (DefaultTreeModel)this.NaviTree.getModel();
    task.setId(this.ipTableTasks.addTaskInfo(task));
    node.setLondId(task.getId());
    this.ipTableTasks.rebuildipTableXmlFile();
    NaviModel.insertNodeInto(newTask, this.datanode, 0);
    return true;
  }

  public void removeTask() throws TransformerConfigurationException, TransformerException, RemoteException {
    DefaultTreeModel NaviModel = (DefaultTreeModel)this.NaviTree.getModel();
    String name = this.tl.getOwnerByTaskId(this.cur_taskId);

    this.tl.removeTask(this.popnode.getLongId());
    this.tl.rebuildTaskXmlFile("config/task.xml");

    if ((this.popnode.getLongId() == this.cur_taskId) && (this.cur_view == this.job_viewer))
    {
      initTaskList(name);

      this.cur_view.setVisible(false);
      this.Controllers.remove(this.cur_view);
      this.cur_view = this.TaskUserList;
      this.Controllers.add(this.cur_view, "Center");
      this.cur_view.setVisible(true);
    }

    NaviModel.removeNodeFromParent(this.datanode);
  }

  public void removeJob() throws TransformerConfigurationException, TransformerException, RemoteException
  {
    DefaultTreeModel NaviModel = (DefaultTreeModel)this.NaviTree.getModel();
    this.job_viewer.removeButton(this.popnode.getLongId(), Boolean.valueOf(false));
    this.tl.removeJob(this.popnode.getLongId());
    this.tl.rebuildTaskXmlFile("config/task.xml");

    NaviModel.removeNodeFromParent(this.datanode);
  }

  public void removeJobById(long jobId, TreeNode node) throws TransformerConfigurationException, TransformerException, RemoteException
  {
    DefaultTreeModel NaviModel = (DefaultTreeModel)this.NaviTree.getModel();

    this.tl.removeJob(jobId);
    this.tl.rebuildTaskXmlFile("config/task.xml");
    NaviModel.removeNodeFromParent((MutableTreeNode)node);
  }
  public void copyTask(PropertyOperation PO, DataOperation DO) throws RemoteException, ExecutionTerminationException, TransformerConfigurationException, TransformerException {
    ArrayList jobTitles = new ArrayList();

    NodeInfo[] jobList = this.tl.getJobList(getTaskId());

    for (int j = 0; j < jobList.length; j++) {
      if (jobList[j].getIdentifier() == 1)
        jobTitles.add(jobList[j].getName());
      else if (jobList[j].getIdentifier() == 35) {
        jobTitles.add(jobList[j].getName());
      }
    }

    Element task = this.tl.getElementByTaskID(getTaskId());
    PO.copyTask(task);

    String[] values = this.ol.getValuesByTagName(task, "data-definition-id");
    Element[] ddls = new Element[values.length];
    Map ddlMap = new HashMap();
    for (int j = 0; j < values.length; j++) {
      ddls[j] = this.ol.getElementByDdlID(Long.valueOf(values[j]).longValue());
      ddlMap.put(jobTitles.get(j), this.ol.getTitle(Integer.valueOf(values[j]).intValue()));
    }
    for (int ddlCount = 0; ddlCount < values.length; ddlCount++) {
      DO.copyDDL(ddls[ddlCount]);
    }

    jobList = PO.getJobList(PO.getNewTaskId() - 1L);
    int j = DO.getNewDDLId() - 1;
    for (int idx = 0; idx < jobList.length; idx++) {
      if (jobList[idx].getIdentifier() == 1) {
        String ddlTitle = (String)ddlMap.get(jobList[idx].getName());
        for (int k = j; k > 0; k--)
          if ((DO.getTitle(k) != null) && (DO.getTitle(k).equals(ddlTitle))) {
            PO.setDataDefinitionId(jobList[idx].getLongId(), k);
            break;
          }
      }
      else if (jobList[idx].getIdentifier() == 35) {
        String ddlTitle = (String)ddlMap.get(jobList[idx].getName());
        for (int k = j; k > 0; k--) {
          if ((DO.getTitle(k) != null) && (DO.getTitle(k).equals(ddlTitle))) {
            PO.setDataDefinitionId(jobList[idx].getLongId(), k);
            break;
          }
        }
      }
    }
    PO.rebuildTaskXmlFile("config/task.xml");

    this.cur_view.setVisible(false);
    this.Controllers.remove(this.cur_view);
    this.cur_view = this.DBMainList;
    this.Controllers.add(this.cur_view, "Center");
    this.cur_view.setVisible(true);
    initTree();
  }

  public void copyDDL(DataOperation DO) throws RemoteException, ExecutionTerminationException {
    Element ddl = this.ol.getElementByDdlID(this.cur_ddl_id);
    DO.copyDDL(ddl);
    this.cur_view.setVisible(false);
    this.Controllers.remove(this.cur_view);
    this.cur_view = this.DBMainList;
    this.Controllers.add(this.cur_view, "Center");
    this.cur_view.setVisible(true);
    initTree();
  }

  public void setLine(long fromId, long toId) throws TransformerConfigurationException, TransformerException, RemoteException
  {
    this.tl.setLine(this.cur_taskId, fromId, toId);
    this.tl.rebuildTaskXmlFile("config/task.xml");
  }

  public Vector<Vector<Long>> getLines() throws RemoteException
  {
    return this.tl.getLines(this.cur_taskId);
  }

  public void setJobLocation(Point currentLocation, long currentJobId) throws TransformerConfigurationException, TransformerException, RemoteException
  {
    this.tl.setJobLocation(this.cur_taskId, currentLocation, currentJobId);
    this.tl.rebuildTaskXmlFile("config/task.xml");
  }

  public PropertyOperation getPropertyOperation()
  {
    return this.tl;
  }

  public IEDeployer getDeployer()
  {
    return this.doo;
  }

  public long getTaskId()
  {
    return this.cur_taskId;
  }

  public NodeInfo getNodeInfo()
  {
    return this.popnode;
  }

  public void switchTaskPopupText(TaskState flag)
  {
    if (flag.equals(TaskState.RUNNING))
    {
      this.item_runTask.setText("Pause Task");
      this.item_stopTask.setEnabled(true);
    }
    else if ((flag.equals(TaskState.INIT)) || (flag.equals(TaskState.END)))
    {
      this.item_runTask.setText("Run Task");
      this.item_stopTask.setEnabled(false);
    }
    else if (flag.equals(TaskState.PAUSE))
    {
      this.item_runTask.setText("Rerun Task");
      this.item_stopTask.setEnabled(true);
    }
  }

  public void switchMapReduceTaskToolBar(TaskState flag) {
    if (flag == null) {
      this.MapReduceRunBtn.setEnabled(false);
      this.MapReduceStopBtn.setEnabled(false);
      return;
    }
    if (flag.equals(TaskState.RUNNING)) {
      this.MapReduceRunBtn.setEnabled(false);
      this.MapReduceStopBtn.setEnabled(true);
    }
    else if (flag.equals(TaskState.END)) {
      this.MapReduceRunBtn.setEnabled(true);
      this.MapReduceStopBtn.setEnabled(false);
    }
  }

  public void switchTaskToolBar(TaskState flag)
  {
    if (flag == null)
    {
      this.TaskRunTBtn.setEnabled(false);
      this.TaskStopTBtn.setEnabled(false);
      this.TaskRerunTBtn.setEnabled(false);
      this.TaskPauseTBtn.setEnabled(false);
      return;
    }

    if (flag.equals(TaskState.RUNNING))
    {
      this.TaskRunTBtn.setEnabled(false);
      this.TaskStopTBtn.setEnabled(true);
      this.TaskRerunTBtn.setEnabled(false);
      this.TaskPauseTBtn.setEnabled(true);
    }
    else if ((flag.equals(TaskState.INIT)) || (flag.equals(TaskState.END)))
    {
      this.TaskRunTBtn.setEnabled(true);
      this.TaskStopTBtn.setEnabled(false);
      this.TaskRerunTBtn.setEnabled(false);
      this.TaskPauseTBtn.setEnabled(false);
    }
    else if (flag.equals(TaskState.PAUSE))
    {
      this.TaskRunTBtn.setEnabled(false);
      this.TaskStopTBtn.setEnabled(true);
      this.TaskRerunTBtn.setEnabled(true);
      this.TaskPauseTBtn.setEnabled(false);
    }
    else
    {
      this.TaskRunTBtn.setEnabled(false);
      this.TaskStopTBtn.setEnabled(false);
      this.TaskRerunTBtn.setEnabled(false);
      this.TaskPauseTBtn.setEnabled(false);
    }
  }

  public void setPopupNodeByTaskId(long taskId) throws RemoteException
  {
    TreeModel model = this.NaviTree.getModel();
    DefaultTreeModel dModel = (DefaultTreeModel)this.NaviTree.getModel();

    DefaultMutableTreeNode root = (DefaultMutableTreeNode)dModel.getRoot();
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(new NodeInfo("", ""));
    NodeInfo info = (NodeInfo)node.getUserObject();

    for (int i = 0; i < root.getChildCount(); i++)
    {
      node = (DefaultMutableTreeNode)root.getChildAt(i);
      info = (NodeInfo)node.getUserObject();

      if (info.getType().equals("tasks"))
      {
        root = node;
        break;
      }
    }

    String name = this.tl.getOwnerByTaskId(taskId);

    for (int i = 0; i < root.getChildCount(); i++)
    {
      node = (DefaultMutableTreeNode)root.getChildAt(i);
      info = (NodeInfo)node.getUserObject();

      if ((info.getType().equals("taskuser")) && (info.getName().equals(name)))
      {
        root = node;
        break;
      }
    }

    for (int i = 0; i < root.getChildCount(); i++)
    {
      node = (DefaultMutableTreeNode)root.getChildAt(i);
      info = (NodeInfo)node.getUserObject();

      if ((info.getType().equals("task")) && (info.getLongId() == taskId))
      {
        this.popnode = info;
        this.datanode = node;
        break;
      }
    }
  }

  public void setPopupNodeByIPTableId(long iptabletaskId)
    throws RemoteException
  {
    TreeModel model = this.NaviTree.getModel();
    DefaultTreeModel dModel = (DefaultTreeModel)this.NaviTree.getModel();

    DefaultMutableTreeNode root = (DefaultMutableTreeNode)dModel.getRoot();
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(new NodeInfo("", ""));
    NodeInfo info = (NodeInfo)node.getUserObject();

    String name = this.ipTableTasks.getTaskInfoById(iptabletaskId).getName();
    boolean flag = false;
    for (int i = 0; i < root.getChildCount(); i++)
    {
      node = (DefaultMutableTreeNode)root.getChildAt(i);
      info = (NodeInfo)node.getUserObject();
      if (info.getType().equals("iptable")) {
        for (int j = 0; j < node.getChildCount(); j++) {
          NodeInfo nodeInfo = (NodeInfo)((DefaultMutableTreeNode)node.getChildAt(j)).getUserObject();
          if (nodeInfo.getName().equals(name)) {
            this.datanode = ((DefaultMutableTreeNode)node.getChildAt(j));
            flag = true;
            break;
          }
        }
        if (flag)
          break;
      }
    }
  }

  public String getTaskType(long taskId) throws RemoteException
  {
    return this.tl.getTaskType(taskId);
  }

  public String getTaskScheduleList(long taskId) throws RemoteException {
    return this.tl.getTaskScheduleList(taskId);
  }

  public void setTaskScheduleList(long taskId, String ShceduleList) throws RemoteException {
    this.tl.setTaskScheduleList(taskId, ShceduleList);
  }

  public int getTypeSize(String type)
  {
    if (type.equals("string"))
    {
      return 0;
    }
    if (type.equals("long"))
    {
      return this.sizeOfLong;
    }
    if (type.equals("double"))
    {
      return this.sizeOfDouble;
    }
    if (type.equals("integer"))
    {
      return this.sizeOfInteger;
    }

    return 0;
  }

  public JPanel getDBMainList()
  {
    return this.DBMainList;
  }
  public JPanel getControllers() {
    return this.Controllers;
  }

  public static void main(String[] args)
  {
    EventQueue.invokeLater(new Runnable()
    {
      public void run()
      {
        try
        {
          new iesMainFrm().setVisible(true);
        }
        catch (ParserConfigurationException ex)
        {
          iesMainFrm.log.debug(ex);
        }
        catch (IOException ex)
        {
          iesMainFrm.log.debug(ex);
        }
        catch (ExecutionTerminationException ex)
        {
          iesMainFrm.log.info(ex);
        }
      }
    });
  }

  class TaskStatePlayer extends Thread
  {
    IEDeployer doo;
    DefaultTableModel model;
    volatile boolean isRunning;

    TaskStatePlayer(IEDeployer doo)
    {
      this.doo = doo;
      this.model = ((DefaultTableModel)iesMainFrm.this.TasksTable1.getModel());
    }

    public void shutdownReuqest()
    {
      interrupt();
      this.isRunning = false;
    }

    public void run()
    {
      this.isRunning = true;
      try
      {
        while (this.isRunning)
        {
          for (int i = 0; i < iesMainFrm.this.taskId_list.size(); i++)
          {
            String str = "";

            TaskState state = TaskState.END;
            try
            {
              state = this.doo.currentState(((Long)iesMainFrm.this.taskId_list.get(i)).longValue());
            }
            catch (ExecutionTerminationException ex)
            {
              iesMainFrm.this.warnMsgDlg(iesMainFrm.this, ex.getMessage());
            }

            switch (iesMainFrm.100.$SwitchMap$dblab$core$task$TaskState[state.ordinal()])
            {
            case 1:
              str = "Running2";
              break;
            case 2:
              str = "Pause";
              break;
            case 3:
            case 4:
              str = "Stopped";
            }

            boolean ext = false;

            for (int j = 0; j < this.model.getRowCount(); j++)
            {
              if (iesMainFrm.this.tl.getNameByTaskId(((Long)iesMainFrm.this.taskId_list.get(i)).longValue()).equals(this.model
                .getValueAt(j, 0)
                .toString()))
              {
                ext = true;
                this.model.setValueAt(str, j, 1);

                break;
              }
            }

            if (!ext)
            {
              this.model.addRow(new Object[] { iesMainFrm.this.tl
                .getNameByTaskId(((Long)iesMainFrm.this.taskId_list
                .get(i))
                .longValue()), str });
            }

          }

          Thread.sleep(1000L);
        }

      }
      catch (InterruptedException ex)
      {
        iesMainFrm.log.info(ex);
      }
      catch (RemoteException ex)
      {
        iesMainFrm.log.info(ex);
      }
    }
  }

  class TaskExecutionMonitor extends Thread
  {
    long taskId;

    TaskExecutionMonitor(long id)
    {
      this.taskId = id;
    }

    public void run()
    {
      TaskState state = TaskState.END;
      try
      {
        state = iesMainFrm.this.doo.currentState(this.taskId);

        while (!state.equals(TaskState.END))
        {
          Thread.sleep(1000L);
          state = iesMainFrm.this.doo.currentState(this.taskId);
        }
      }
      catch (RemoteException ex)
      {
        iesMainFrm.log.info(ex);
      }
      catch (ExecutionTerminationException ex)
      {
        iesMainFrm.log.info(ex);
        iesMainFrm.this.switchTaskToolBar(TaskState.END);
        iesMainFrm.this.taskId_list.remove(Long.valueOf(this.taskId));
        iesMainFrm.this.warnMsgDlg(iesMainFrm.this, ex.getMessage());
      }
      catch (InterruptedException ex)
      {
        iesMainFrm.log.info(ex);
      }
    }
  }

  private class NaviRenderer extends DefaultTreeCellRenderer
  {
    ImageIcon icn_db;
    ImageIcon icn_user;
    ImageIcon icn_ddl;
    ImageIcon icn_tasks;
    ImageIcon icn_table;
    ImageIcon icn_taskuser;
    ImageIcon icn_views;
    ImageIcon icn_job;
    ImageIcon icn_pipes;

    public NaviRenderer()
    {
      this.icn_db = new ImageIcon(getClass().getResource("/dbies/imgs/db.gif"));
      this.icn_user = new ImageIcon(getClass().getResource("/dbies/imgs/user.gif"));
      this.icn_ddl = new ImageIcon(getClass().getResource("/dbies/imgs/ddl.gif"));
      this.icn_tasks = new ImageIcon(getClass().getResource("/dbies/imgs/tasks.gif"));
      this.icn_table = new ImageIcon(getClass().getResource("/dbies/imgs/table.gif"));
      this.icn_taskuser = new ImageIcon(getClass().getResource("/dbies/imgs/taskuser.gif"));
      this.icn_views = new ImageIcon(getClass().getResource("/dbies/imgs/views.gif"));
      this.icn_job = new ImageIcon(getClass().getResource("/dbies/imgs/job.gif"));
      this.icn_pipes = new ImageIcon(getClass().getResource("/dbies/imgs/pipes.gif"));
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
      super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

      DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;
      NodeInfo type = (NodeInfo)node.getUserObject();

      if (type.getType().equals("db"))
      {
        setIcon(this.icn_db);
      }
      else if (type.getType().equals("user"))
      {
        setIcon(this.icn_user);
      }
      else if (type.getType().equals("dd"))
      {
        setIcon(this.icn_ddl);
      }
      else if (type.getType().equals("ddl"))
      {
        setIcon(this.icn_table);
      }
      else if (type.getType().equals("tasks"))
      {
        setIcon(this.icn_tasks);
      }
      else if (type.getType().equals("taskuser"))
      {
        setIcon(this.icn_taskuser);
      }
      else if (type.getType().equals("task"))
      {
        setIcon(this.icn_views);
      }
      else if (type.getType().equals("job"))
      {
        setIcon(this.icn_job);
      }
      else if (type.getType().equals("pipes"))
      {
        setIcon(this.icn_pipes);
      }
      else if (type.getType().equals("iptable"))
      {
        setIcon(this.icn_table);
      }

      return this;
    }
  }

  public class ExportSystemInfoTimerTask extends TimerTask
  {
    public long cur_TaskID = -1L;
    public boolean onOldMem = true;
    public int exportOldMem = 0;
    public int exportUsedMem = 0;
    public int exportMaxUsedMem = 0;
    public String taskName = "task";
    public long exportEndTime = 0L;
    public String exportStrEndTime = "";
    public long oldExportEndTime = 0L;
    public int exportRepeatCount = 0;
    public int exportChangeCount = 0;
    long[] exportStartTimeArray;
    String[] exportStrStartTimeArray;
    long[] exportInputRateArray;
    boolean exportFlag = false;
    int exportBaseCount = 0;
    int exportOutputCount = 0;

    ArrayList<Integer> exportSplitterMaxUsedMem = new ArrayList();
    ArrayList<Integer> exportMapperMaxUsedMem = new ArrayList();
    ArrayList<Integer> exportReducerMaxUsedMem = new ArrayList();
    ArrayList<Boolean> exportSplitterOnOldMem = new ArrayList();
    ArrayList<Boolean> exportMapperOnOldMem = new ArrayList();
    ArrayList<Boolean> exportReducerOnOldMem = new ArrayList();
    ArrayList<Integer> exportSplitterOldMem = new ArrayList();
    ArrayList<Integer> exportMapperOldMem = new ArrayList();
    ArrayList<Integer> exportReducerOldMem = new ArrayList();
    ArrayList<Integer> exportSplitterUsedMem = new ArrayList();
    ArrayList<Integer> exportMapperUsedMem = new ArrayList();
    ArrayList<Integer> exportReducerUsedMem = new ArrayList();
    ArrayList<Double> exportSplitterSumCPU = new ArrayList();
    ArrayList<Double> exportMapperSumCPU = new ArrayList();
    ArrayList<Double> exportReducerSumCPU = new ArrayList();
    ArrayList<Integer> exportSplitterCountCPU = new ArrayList();
    ArrayList<Integer> exportMapperCountCPU = new ArrayList();
    ArrayList<Integer> exportReducerCountCPU = new ArrayList();
    public DataOperation[] exportSplitterDO;
    public DataOperation[] exportMapperDO;
    public DataOperation[] exportReducerDO;
    public PropertyOperation[] exportSplitterPO;
    public PropertyOperation[] exportMapperPO;
    public PropertyOperation[] exportReducerPO;
    public IEDeployer[] exportSplitterIED;
    public IEDeployer[] exportMapperIED;
    public IEDeployer[] exportReducerIED;

    public ExportSystemInfoTimerTask()
    {
    }

    public void setExportRMI()
    {
      this.exportSplitterDO = RMI_Controller.splitterDataOperations;
      this.exportMapperDO = RMI_Controller.mapperDataOperations;
      this.exportReducerDO = RMI_Controller.reducerDataOperations;
      this.exportSplitterPO = RMI_Controller.splitterPropertyOperations;
      this.exportMapperPO = RMI_Controller.mapperPropertyOperations;
      this.exportReducerPO = RMI_Controller.reducerPropertyOperations;
      this.exportSplitterIED = RMI_Controller.splitterDeployers;
      this.exportMapperIED = RMI_Controller.mapperDeployers;
      this.exportReducerIED = RMI_Controller.reducerDeployers;
      if (this.exportSplitterDO != null) {
        for (int i = 0; i < this.exportSplitterDO.length; i++) {
          this.exportSplitterMaxUsedMem.add(Integer.valueOf(0));
          this.exportSplitterSumCPU.add(Double.valueOf(0.0D));
          this.exportSplitterCountCPU.add(Integer.valueOf(0));
          this.exportSplitterOnOldMem.add(Boolean.valueOf(true));
          this.exportSplitterOldMem.add(Integer.valueOf(0));
          this.exportSplitterUsedMem.add(Integer.valueOf(0));
        }
      }
      if (this.exportMapperDO != null) {
        for (int i = 0; i < this.exportMapperDO.length; i++) {
          this.exportMapperMaxUsedMem.add(Integer.valueOf(0));
          this.exportMapperSumCPU.add(Double.valueOf(0.0D));
          this.exportMapperCountCPU.add(Integer.valueOf(0));
          this.exportMapperOnOldMem.add(Boolean.valueOf(true));
          this.exportMapperOldMem.add(Integer.valueOf(0));
          this.exportMapperUsedMem.add(Integer.valueOf(0));
        }
      }
      if (this.exportReducerDO != null)
        for (int i = 0; i < this.exportReducerDO.length; i++) {
          this.exportReducerMaxUsedMem.add(Integer.valueOf(0));
          this.exportReducerSumCPU.add(Double.valueOf(0.0D));
          this.exportReducerCountCPU.add(Integer.valueOf(0));
          this.exportReducerOnOldMem.add(Boolean.valueOf(true));
          this.exportReducerOldMem.add(Integer.valueOf(0));
          this.exportReducerUsedMem.add(Integer.valueOf(0));
        }
    }

    public void setParam(long[] array, String[] strArray, int baseCount)
    {
      for (int i = 0; i < baseCount; i++) {
        array[i] = 0L;
        strArray[i] = "";
      }
    }

    public void exportMapReduceWorkersInfo() throws RemoteException {
      ArrayList taskIDs = new ArrayList();
      taskIDs = iesMainFrm.this.tl.getMapReduceMasterInfo(this.cur_TaskID, "splitter-taskids", "splitter-taskid", "taskid");
      if ((taskIDs != null) && (taskIDs.size() > 0)) {
        for (int i = 0; i < this.exportSplitterPO.length; i++) {
          export(this.exportSplitterDO[i], this.exportSplitterPO[i], this.exportSplitterIED[i], Long.valueOf((String)taskIDs.get(i)).longValue(), this.exportSplitterMaxUsedMem, this.exportSplitterUsedMem, this.exportSplitterOnOldMem, this.exportSplitterOldMem, this.exportSplitterSumCPU, this.exportSplitterCountCPU, i);
        }
      }

      taskIDs = iesMainFrm.this.tl.getMapReduceMasterInfo(this.cur_TaskID, "mapper-taskids", "mapper-taskid", "taskid");
      if ((taskIDs != null) && (taskIDs.size() > 0)) {
        for (int i = 0; i < this.exportMapperPO.length; i++) {
          export(this.exportMapperDO[i], this.exportMapperPO[i], this.exportMapperIED[i], Long.valueOf((String)taskIDs.get(i)).longValue(), this.exportMapperMaxUsedMem, this.exportMapperUsedMem, this.exportMapperOnOldMem, this.exportMapperOldMem, this.exportMapperSumCPU, this.exportMapperCountCPU, i);
        }
      }

      taskIDs = iesMainFrm.this.tl.getMapReduceMasterInfo(this.cur_TaskID, "reducer-taskids", "reducer-taskid", "taskid");
      if ((taskIDs != null) && (taskIDs.size() > 0))
        for (int i = 0; i < this.exportReducerPO.length; i++)
          export(this.exportReducerDO[i], this.exportReducerPO[i], this.exportReducerIED[i], Long.valueOf((String)taskIDs.get(i)).longValue(), this.exportReducerMaxUsedMem, this.exportReducerUsedMem, this.exportReducerOnOldMem, this.exportReducerOldMem, this.exportReducerSumCPU, this.exportReducerCountCPU, i);
    }

    public void export(DataOperation DO, PropertyOperation PO, IEDeployer IED, long taskID, ArrayList<Integer> maxMem, ArrayList<Integer> usedMem, ArrayList<Boolean> onOldMem, ArrayList<Integer> OldMem, ArrayList<Double> SumCPU, ArrayList<Integer> CountCPU, int listIndex)
    {
      try
      {
        if (taskID != -1L) {
          SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
          DecimalFormat format = new DecimalFormat(".##");
          double CpuUsage = PO.getCPUInfo();
          ArrayList memInfo = PO.getMemInfo();
          double MemUsage = Double.parseDouble(format.format(memInfo.get(0)));
          long CurrentSystemTime = PO.getCurrentSystemTime();

          String TaskName = PO.getNameByTaskId(taskID);
          NodeInfo[] jobList = PO.getJobList(taskID);
          ArrayList baseNames = new ArrayList();
          int baseCount = 0;
          for (int i = 0; i < jobList.length; i++) {
            if (jobList[i].getIdentifier() == 1) {
              baseNames.add(PO.getJobNameById(jobList[i].getLongId()));
              baseCount++;
            }
          }
          long[] startTimeArray = new long[baseCount];
          String[] strStartTimeArray = new String[baseCount];
          setParam(startTimeArray, strStartTimeArray, baseCount);
          long[] inputRateArray = new long[baseCount];
          int j = 0;
          String txtStartTime = "";
          String txtInputRate = "";
          long endTime = 0L;
          String strEndTime = "";
          int outputCount = 0;
          for (int i = 0; i < jobList.length; i++) {
            if (jobList[i].getIdentifier() == 1) {
              startTimeArray[j] = IED.getStartTime(jobList[i].getLongId());
              strStartTimeArray[j] = sdfNow.format(new Date(startTimeArray[j]));
              txtStartTime = txtStartTime + "[" + (String)baseNames.get(j) + "]" + strStartTimeArray[j] + "(" + startTimeArray[j] + ") / ";
              inputRateArray[j] = IED.getTupleCount(jobList[i].getLongId());
              txtInputRate = txtInputRate + "[" + (String)baseNames.get(j) + "]" + inputRateArray[j] + " / ";
              j++;
            } else if (jobList[i].getIdentifier() == 15) {
              endTime = IED.getEndTime(jobList[i].getLongId());
              strEndTime = sdfNow.format(new Date(endTime));
              outputCount = IED.getOutputCount(jobList[i].getLongId());
            }
          }
          boolean flag = true;
          for (int i = 0; i < baseCount; i++) {
            if (startTimeArray[i] == 0L) {
              flag = false;
              break;
            }
          }
          if (flag) {
            String fileName = iesMainFrm.this.exportLocationTextField.getText() + TaskName + ".txt";
            String outputContents = null;
            try
            {
              FileWriter file = new FileWriter(fileName, true);
              outputContents = CurrentSystemTime + "\t";
              for (int i = 0; i < baseCount; i++) {
                outputContents = outputContents + startTimeArray[i] + "\t";
                outputContents = outputContents + inputRateArray[i] + "\t";
              }
              CountCPU.set(listIndex, CountCPU.get(listIndex));
              SumCPU.set(listIndex, Double.valueOf(((Double)SumCPU.get(listIndex)).doubleValue() + CpuUsage));
              if (((Boolean)onOldMem.get(listIndex)).booleanValue()) {
                OldMem.set(listIndex, Integer.valueOf(((Double)memInfo.get(1)).intValue()));
                onOldMem.set(listIndex, Boolean.valueOf(false));
              }
              if (((Integer)maxMem.get(listIndex)).intValue() <= ((Integer)OldMem.get(listIndex)).intValue() - ((Double)memInfo.get(1)).intValue()) {
                maxMem.set(listIndex, Integer.valueOf(((Integer)OldMem.get(listIndex)).intValue() - ((Double)memInfo.get(1)).intValue()));
              }

              outputContents = outputContents + endTime + "\t" + CpuUsage + "\t" + new DecimalFormat(".##")
                .format(((Double)SumCPU
                .get(listIndex))
                .doubleValue() / ((Integer)CountCPU.get(listIndex)).intValue()) + MemUsage + "\t" + maxMem
                .get(listIndex) +
                "\t" + outputCount + "\n";

              file.write(outputContents);
              file.close();
            } catch (IOException ex) {
              java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
            }
          }
        }
      } catch (RemoteException localRemoteException) {
      }
    }

    public void run() {
      try {
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        if (this.cur_TaskID != -1L) {
          if (iesMainFrm.this.isMasterOfMapReduce(this.cur_TaskID)) {
            exportMapReduceWorkersInfo();
          }
          this.taskName = iesMainFrm.this.tl.getNameByTaskId(this.cur_TaskID);
          NodeInfo[] jobList = iesMainFrm.this.tl.getJobList(this.cur_TaskID);
          ArrayList baseNames = new ArrayList();
          this.exportBaseCount = 0;
          for (int i = 0; i < jobList.length; i++) {
            if (jobList[i].getIdentifier() == 1) {
              baseNames.add(iesMainFrm.this.tl.getJobNameById(jobList[i].getLongId()));
              this.exportBaseCount += 1;
            }
          }
          this.exportStartTimeArray = new long[this.exportBaseCount];
          this.exportStrStartTimeArray = new String[this.exportBaseCount];
          setParam(this.exportStartTimeArray, this.exportStrStartTimeArray, this.exportBaseCount);
          this.exportInputRateArray = new long[this.exportBaseCount];
          int j = 0;
          String txtStartTime = "";
          String txtInputRate = "";
          for (int i = 0; i < jobList.length; i++) {
            if (jobList[i].getIdentifier() == 1) {
              this.exportStartTimeArray[j] = iesMainFrm.this.doo.getStartTime(jobList[i].getLongId());
              this.exportStrStartTimeArray[j] = sdfNow.format(new Date(this.exportStartTimeArray[j]));
              txtStartTime = txtStartTime + "[" + (String)baseNames.get(j) + "]" + this.exportStrStartTimeArray[j] + "(" + this.exportStartTimeArray[j] + ") / ";
              this.exportInputRateArray[j] = iesMainFrm.this.doo.getTupleCount(jobList[i].getLongId());
              txtInputRate = txtInputRate + "[" + (String)baseNames.get(j) + "]" + this.exportInputRateArray[j] + " / ";
              j++;
            } else if (jobList[i].getIdentifier() == 15) {
              this.exportEndTime = iesMainFrm.this.doo.getEndTime(jobList[i].getLongId());
              this.exportStrEndTime = sdfNow.format(new Date(this.exportEndTime));
              this.exportOutputCount = iesMainFrm.this.doo.getOutputCount(jobList[i].getLongId());
              int repeatCount = Integer.valueOf(iesMainFrm.this.autoTerminateRepeatTextField.getText()).intValue();
              int changeCount = Integer.valueOf(iesMainFrm.this.autoTerminateChangeTextField.getText()).intValue();
              if ((repeatCount > 0) && (this.exportEndTime != 0L) && (this.exportEndTime == this.oldExportEndTime)) {
                this.exportRepeatCount += 1;
                iesMainFrm.this.autoTerminateTupleRepeatLabel.setText("(repeat count: " + this.exportRepeatCount + ")");
              } else {
                this.exportRepeatCount = 0;
                iesMainFrm.this.autoTerminateTupleRepeatLabel.setText("(repeat count: 0)");
              }
              if ((changeCount > 0) && (this.exportEndTime != 0L)) {
                this.exportChangeCount = iesMainFrm.this.doo.getOutputCount(jobList[i].getLongId());
                iesMainFrm.this.autoTerminateOutputCountLabel.setText("(change count: " + this.exportChangeCount + ")");
              }
              if ((repeatCount > 0) && (this.exportRepeatCount > repeatCount)) {
                iesMainFrm.this.setExportTimer(this.cur_TaskID);
              }
              if ((changeCount > 0) && (this.exportChangeCount == changeCount)) {
                iesMainFrm.this.setExportTimer(this.cur_TaskID);
              }
              this.oldExportEndTime = this.exportEndTime;
            }
          }
          this.exportFlag = true;
          for (int i = 0; i < this.exportBaseCount; i++) {
            if (this.exportStartTimeArray[i] == 0L) {
              this.exportFlag = false;
              break;
            }
          }
        }
        if (this.exportFlag) {
          String fileName = iesMainFrm.this.exportLocationTextField.getText() + this.taskName + ".txt";
          String outputContents = null;
          try
          {
            FileWriter file = new FileWriter(fileName, true);
            outputContents = iesMainFrm.this.currentSystemTime + "\t";
            for (int i = 0; i < this.exportBaseCount; i++) {
              outputContents = outputContents + this.exportStartTimeArray[i] + "\t";
              outputContents = outputContents + this.exportInputRateArray[i] + "\t";
            }
            iesMainFrm.this.countCpuUsage += 1;
            iesMainFrm.this.sumCpuUsage += iesMainFrm.this.cpuUsage;
            if (this.onOldMem) {
              this.exportOldMem = ((Double)iesMainFrm.this.memInfo.get(1)).intValue();
              this.onOldMem = false;
            }
            if (this.exportMaxUsedMem <= this.exportOldMem - ((Double)iesMainFrm.this.memInfo.get(1)).intValue()) {
              this.exportMaxUsedMem = (this.exportOldMem - ((Double)iesMainFrm.this.memInfo.get(1)).intValue());
            }
            outputContents = outputContents + this.exportEndTime + "\t" + iesMainFrm.this.cpuUsage + "\t" + new DecimalFormat(".##")
              .format(iesMainFrm.this.sumCpuUsage / iesMainFrm.this.countCpuUsage) +
              "\t" + iesMainFrm.this.memUsage + "\t" + this.exportMaxUsedMem + "\t" + this.exportOutputCount + "\n";

            file.write(outputContents);
            file.close();
          } catch (IOException ex) {
            java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
      } catch (RemoteException ex) {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
  }

  public class SystemInfoTimerTask extends TimerTask
  {
    long[] startTimeArray;
    String[] strStartTimeArray;
    public long endTime = 0L;
    public String strEndTime = "";
    long[] inputRateArray;
    long oldTaskID = -1L;
    long oldMasterTaskID = -1L;
    boolean masterOnFlag = false;
    NodeInfo[] jobListForEndTime;

    public SystemInfoTimerTask()
    {
    }

    public void setParam(long[] array, String[] strArray, int baseCount)
    {
      for (int i = 0; i < baseCount; i++) {
        array[i] = 0L;
        strArray[i] = "";
      }
    }

    public void setMapReduceComboBox() throws RemoteException {
      DefaultComboBoxModel model = (DefaultComboBoxModel)iesMainFrm.this.mapReduceSIMComboBox.getModel();
      model.removeAllElements();
      iesMainFrm.this.mapReduceSIMComboBox.addItem("[Master-Splitter]: " + Servers.SERVER_IP);
      ArrayList ips = new ArrayList();
      ArrayList suffixes = new ArrayList();
      ArrayList taskIDs = new ArrayList();
      ips = iesMainFrm.this.tl.getMapReduceMasterInfo(iesMainFrm.this.cur_taskId, "splitter-ips", "splitter-ip", "ip");
      suffixes = iesMainFrm.this.tl.getMapReduceMasterInfo(iesMainFrm.this.cur_taskId, "splitter-suffixes", "splitter-suffix", "suffix");
      taskIDs = iesMainFrm.this.tl.getMapReduceMasterInfo(iesMainFrm.this.cur_taskId, "splitter-taskids", "splitter-taskid", "taskid");
      if ((ips != null) && (ips.size() > 0)) {
        for (int i = 0; i < ips.size(); i++) {
          iesMainFrm.this.mapReduceSIMComboBox.addItem("[Slave-Splitter]: " + (String)ips.get(i) + " " + (String)suffixes.get(i) + " " + (String)taskIDs.get(i));
        }
      }
      ips = iesMainFrm.this.tl.getMapReduceMasterInfo(iesMainFrm.this.cur_taskId, "mapper-ips", "mapper-ip", "ip");
      suffixes = iesMainFrm.this.tl.getMapReduceMasterInfo(iesMainFrm.this.cur_taskId, "mapper-suffixes", "mapper-suffix", "suffix");
      taskIDs = iesMainFrm.this.tl.getMapReduceMasterInfo(iesMainFrm.this.cur_taskId, "mapper-taskids", "mapper-taskid", "taskid");
      if ((ips != null) && (ips.size() > 0)) {
        for (int i = 0; i < ips.size(); i++) {
          iesMainFrm.this.mapReduceSIMComboBox.addItem("[Slave-Mapper]: " + (String)ips.get(i) + " " + (String)suffixes.get(i) + " " + (String)taskIDs.get(i));
        }
      }
      ips = iesMainFrm.this.tl.getMapReduceMasterInfo(iesMainFrm.this.cur_taskId, "reducer-ips", "reducer-ip", "ip");
      suffixes = iesMainFrm.this.tl.getMapReduceMasterInfo(iesMainFrm.this.cur_taskId, "reducer-suffixes", "reducer-suffix", "suffix");
      taskIDs = iesMainFrm.this.tl.getMapReduceMasterInfo(iesMainFrm.this.cur_taskId, "reducer-taskids", "reducer-taskid", "taskid");
      if ((ips != null) && (ips.size() > 0)) {
        for (int i = 0; i < ips.size(); i++) {
          iesMainFrm.this.mapReduceSIMComboBox.addItem("[Slave-Reducer]: " + (String)ips.get(i) + " " + (String)suffixes.get(i) + " " + (String)taskIDs.get(i));
        }
      }
      iesMainFrm.this.mapReduceSIMComboBox.setEnabled(true);
    }

    public void run()
    {
      try {
        if (this.oldTaskID != iesMainFrm.this.cur_taskId)
          iesMainFrm.this.exportButton.setEnabled(true);
        String taskName = "task";
        DecimalFormat format = new DecimalFormat(".##");
        iesMainFrm.this.cpuUsage = iesMainFrm.this.tl.getCPUInfo();
        iesMainFrm.this.memInfo = iesMainFrm.this.tl.getMemInfo();
        iesMainFrm.this.memUsage = Double.parseDouble(format.format(iesMainFrm.this.memInfo.get(0)));
        iesMainFrm.this.currentSystemTime = iesMainFrm.this.tl.getCurrentSystemTime();

        if (iesMainFrm.this.mapReduceRMI_InfoFlag) {
          iesMainFrm.this.cpuUsage = iesMainFrm.this.mapReducePO.getCPUInfo();
          iesMainFrm.this.memUsage = Double.parseDouble(format.format(iesMainFrm.this.mapReducePO.getMemInfo().get(0)));
          iesMainFrm.this.currentSystemTime = iesMainFrm.this.mapReducePO.getCurrentSystemTime();
        }

        iesMainFrm.this.cpuUsageLabel.setText(String.valueOf(iesMainFrm.this.cpuUsage) + " %");
        iesMainFrm.this.memoryUsageLabel.setText(String.valueOf(iesMainFrm.this.memUsage) + " %");
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        iesMainFrm.this.strSystemTime = sdfNow.format(new Date(iesMainFrm.this.currentSystemTime));
        iesMainFrm.this.systemTimeLabel.setText(iesMainFrm.this.strSystemTime + " (" + iesMainFrm.this.currentSystemTime + ")");

        if ((iesMainFrm.this.mapReduceRMI_InfoFlag) && (iesMainFrm.this.selectedTaskID != -1)) {
          taskName = iesMainFrm.this.mapReducePO.getNameByTaskId(iesMainFrm.this.selectedTaskID);
          iesMainFrm.this.taskNameTextField.setText(taskName);
          NodeInfo[] jobList = iesMainFrm.this.mapReducePO.getJobList(iesMainFrm.this.selectedTaskID);

          iesMainFrm.this.baseCount = 0;
          ArrayList baseNames = new ArrayList();
          for (int i = 0; i < jobList.length; i++) {
            if (jobList[i].getIdentifier() == 1) {
              baseNames.add(iesMainFrm.this.mapReducePO.getJobNameById(jobList[i].getLongId()));
              iesMainFrm.this.baseCount += 1;
            }
          }
          this.startTimeArray = new long[iesMainFrm.this.baseCount];
          this.strStartTimeArray = new String[iesMainFrm.this.baseCount];
          setParam(this.startTimeArray, this.strStartTimeArray, iesMainFrm.this.baseCount);
          this.inputRateArray = new long[iesMainFrm.this.baseCount];
          int j = 0;
          String txtStartTime = "";
          String txtInputRate = "";
          for (int i = 0; i < jobList.length; i++) {
            if (jobList[i].getIdentifier() == 1) {
              this.startTimeArray[j] = iesMainFrm.this.mapReduceIED.getStartTime(jobList[i].getLongId());
              this.strStartTimeArray[j] = sdfNow.format(new Date(this.startTimeArray[j]));
              txtStartTime = txtStartTime + "[" + (String)baseNames.get(j) + "]" + this.strStartTimeArray[j] + "(" + this.startTimeArray[j] + ") / ";
              this.inputRateArray[j] = iesMainFrm.this.mapReduceIED.getTupleCount(jobList[i].getLongId());
              txtInputRate = txtInputRate + "[" + (String)baseNames.get(j) + "]" + this.inputRateArray[j] + " / ";
              j++;
            } else if (jobList[i].getIdentifier() == 15) {
              this.endTime = iesMainFrm.this.mapReduceIED.getEndTime(jobList[i].getLongId());
              this.strEndTime = sdfNow.format(new Date(this.endTime));
              iesMainFrm.this.endTimeLabel.setText(this.strEndTime + " (" + String.valueOf(this.endTime) + " msec)");
              iesMainFrm.this.elapsedTimeLabel.setText(String.valueOf((this.endTime - this.startTimeArray[0]) * 0.001D));
            }
          }
          iesMainFrm.this.startTimeTextField.setText(txtStartTime);
          iesMainFrm.this.inputRateLabel.setText(txtInputRate);
        }
        else if (iesMainFrm.this.cur_taskId != -1L) {
          taskName = iesMainFrm.this.tl.getNameByTaskId(iesMainFrm.this.cur_taskId);
          iesMainFrm.this.taskNameTextField.setText(taskName);
          NodeInfo[] jobList = iesMainFrm.this.tl.getJobList(iesMainFrm.this.cur_taskId);

          iesMainFrm.this.baseCount = 0;
          ArrayList baseNames = new ArrayList();
          for (int i = 0; i < jobList.length; i++) {
            if (jobList[i].getIdentifier() == 1) {
              baseNames.add(iesMainFrm.this.tl.getJobNameById(jobList[i].getLongId()));
              iesMainFrm.this.baseCount += 1;
            }
          }
          this.startTimeArray = new long[iesMainFrm.this.baseCount];
          this.strStartTimeArray = new String[iesMainFrm.this.baseCount];
          setParam(this.startTimeArray, this.strStartTimeArray, iesMainFrm.this.baseCount);
          this.inputRateArray = new long[iesMainFrm.this.baseCount];
          int j = 0;
          String txtStartTime = "";
          String txtInputRate = "";
          String queueInfo = "";
          String executionState = "";
          for (int i = 0; i < jobList.length; i++) {
            queueInfo = queueInfo + "[" + jobList[i].getName() + "] ";
            queueInfo = queueInfo + String.valueOf(iesMainFrm.this.doo.getInputHandlerQueueSize(jobList[i].getLongId())) + " ";
            queueInfo = queueInfo + String.valueOf(iesMainFrm.this.doo.getOutputHandlerQueueSize(jobList[i].getLongId())) + " ";
            executionState = executionState + "[" + jobList[i].getName() + "] ";
            executionState = executionState + iesMainFrm.this.doo.getExecutionState(jobList[i].getLongId()) + " ";
            if (jobList[i].getIdentifier() == 1) {
              this.startTimeArray[j] = iesMainFrm.this.doo.getStartTime(jobList[i].getLongId());
              this.strStartTimeArray[j] = sdfNow.format(new Date(this.startTimeArray[j]));
              txtStartTime = txtStartTime + "[" + (String)baseNames.get(j) + "]" + this.strStartTimeArray[j] + "(" + this.startTimeArray[j] + ") / ";
              this.inputRateArray[j] = iesMainFrm.this.doo.getTupleCount(jobList[i].getLongId());
              txtInputRate = txtInputRate + "[" + (String)baseNames.get(j) + "]" + this.inputRateArray[j] + " / ";
              j++;
            } else if (jobList[i].getIdentifier() == 15) {
              this.endTime = iesMainFrm.this.doo.getEndTime(jobList[i].getLongId());
              this.strEndTime = sdfNow.format(new Date(this.endTime));
              iesMainFrm.this.endTimeLabel.setText(this.strEndTime + " (" + String.valueOf(this.endTime) + " msec)");
              iesMainFrm.this.elapsedTimeLabel.setText(String.valueOf((this.endTime - this.startTimeArray[0]) * 0.001D));
            }
          }
          iesMainFrm.this.startTimeTextField.setText(txtStartTime);
          iesMainFrm.this.inputRateLabel.setText(txtInputRate);
          iesMainFrm.this.queueInfoLabel.setText(queueInfo);
          iesMainFrm.this.executionStateLabel.setText(executionState);
          if (iesMainFrm.this.isMasterOfMapReduce(iesMainFrm.this.cur_taskId)) {
            if (this.oldMasterTaskID != iesMainFrm.this.cur_taskId) {
              iesMainFrm.this.exportButton.setEnabled(true);
              setMapReduceComboBox();
              this.masterOnFlag = true;
            }
            if (!this.masterOnFlag) {
              setMapReduceComboBox();
              this.masterOnFlag = true;
            }
            ArrayList taskIDs = new ArrayList();
            taskIDs = iesMainFrm.this.tl.getMapReduceMasterInfo(iesMainFrm.this.cur_taskId, "reducer-taskids", "reducer-taskid", "taskid");
            this.jobListForEndTime = RMI_Controller.reducerPropertyOperations[0].getJobList(Long.valueOf((String)taskIDs.get(0)).longValue());
            for (int i = 0; i < this.jobListForEndTime.length; i++) {
              if (this.jobListForEndTime[i].getIdentifier() == 15) {
                this.endTime = RMI_Controller.reducerDeployers[0].getEndTime(this.jobListForEndTime[i].getLongId());
                this.endTime += iesMainFrm.this.getDiffTime(iesMainFrm.this.tl, RMI_Controller.reducerPropertyOperations[0]);
                this.strEndTime = sdfNow.format(new Date(this.endTime));
                iesMainFrm.this.endTimeLabel.setText(this.strEndTime + " (" + String.valueOf(this.endTime) + " msec)");
                iesMainFrm.this.elapsedTimeLabel.setText(String.valueOf((this.endTime - this.startTimeArray[0]) * 0.001D));
              }
            }
            this.oldMasterTaskID = iesMainFrm.this.cur_taskId;
          } else {
            this.masterOnFlag = false;
            iesMainFrm.this.mapReduceSIMComboBox.setEnabled(false);
          }

          if (!iesMainFrm.this.exportTaskIDList.isEmpty()) {
            if (iesMainFrm.this.exportTaskIDList.containsKey(Long.valueOf(iesMainFrm.this.cur_taskId))) {
              if (((Boolean)iesMainFrm.this.exportTaskIDList.get(Long.valueOf(iesMainFrm.this.cur_taskId))).booleanValue())
                iesMainFrm.this.exportButton.setText("Stop Export");
              else {
                iesMainFrm.this.exportButton.setText("Start Export");
              }
            }
            else {
              iesMainFrm.this.exportButton.setText("Start Export");
            }
          }
        }
        if (iesMainFrm.this.onViewGraph)
          iesMainFrm.this.actionGraph(iesMainFrm.this.cpuUsage, iesMainFrm.this.memUsage, this.inputRateArray);
      }
      catch (RemoteException ex) {
        java.util.logging.Logger.getLogger(iesMainFrm.class.getName()).log(Level.SEVERE, null, ex);
      }
      this.oldTaskID = iesMainFrm.this.cur_taskId;
    }
  }
}
