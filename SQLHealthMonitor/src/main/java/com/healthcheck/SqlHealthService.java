package com.healthcheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.DriverManager;




import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.LinkedHashSet;
import java.util.Set;


@Service


public class SqlHealthService {
	@Autowired
	private AppConfig appConfig;
	
	private volatile boolean refreshRunning = false;

	public boolean isRefreshRunning() {

	    return refreshRunning;
	}
/*
    public String getServerName() {

        try {

            Connection con = DBConnection.getConnection();

            Statement stmt = con.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT @@SERVERNAME AS ServerName");

            if (rs.next()) {

                return rs.getString("ServerName");
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return "Unknown";
    }
    
//    Server details
    public HealthReport getHealthReport() {

        HealthReport report = new HealthReport();

        try {

            Connection con = DBConnection.getConnection();

            Statement stmt = con.createStatement();

            ResultSet rs =
            stmt.executeQuery(
            "SELECT @@SERVERNAME AS ServerName,"
          + "@@VERSION AS Version");

            if(rs.next()) {

                report.setServerName(
                        rs.getString("ServerName"));

                report.setVersion(
                        rs.getString("Version"));
            }

            con.close();

        }
        catch(Exception ex) {

            ex.printStackTrace();
        }

        return report;
    }
    
//   This  function is to get the DB status if it online or not
    
    public List<DatabaseInfo> getDatabases() {

        List<DatabaseInfo> databases =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            Statement stmt =
                    con.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                    "SELECT name,state_desc " +
                    "FROM sys.databases " +
                    "ORDER BY name");

            while(rs.next()) {

                DatabaseInfo db =
                        new DatabaseInfo();

                db.setName(
                        rs.getString("name"));

                db.setStatus(
                        rs.getString("state_desc"));

                databases.add(db);
            }

            con.close();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return databases;
    }
    
//  This method will show AG status of server 
    
    public List<AGInfo> getAGStatus() {

        List<AGInfo> agList = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            Statement stmt = con.createStatement();

            String query =
                    "SELECT " +
                    "ag.name AS AGName, " +
                    "ars.role_desc, " +
                    "ars.synchronization_health_desc " +
                    "FROM sys.dm_hadr_availability_replica_states ars " +
                    "INNER JOIN sys.availability_groups ag " +
                    "ON ars.group_id = ag.group_id";

            ResultSet rs = stmt.executeQuery(query);

            while(rs.next()) {

                AGInfo ag = new AGInfo();

                ag.setAgName(rs.getString("AGName"));
                ag.setRole(rs.getString("role_desc"));
                ag.setSyncStatus(
                        rs.getString("synchronization_health_desc"));

                agList.add(ag);
            }

            con.close();

        } catch(Exception e) {

            // SQL Server not using AG
            System.out.println("AG Not Configured");
        }

        return agList;
    }
    
//  This will show the cluster status of the server 
    
    public List<ClusterInfo> getClusterStatus() {

        List<ClusterInfo> clusterList =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            Statement stmt =
                    con.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                    "SELECT NodeName, status_description " +
                    "FROM sys.dm_os_cluster_nodes");

            while(rs.next()) {

                ClusterInfo node =
                        new ClusterInfo();

                node.setNodeName(
                        rs.getString("NodeName"));

                node.setNodeStatus(
                        rs.getString("status_description"));

                clusterList.add(node);
            }

            con.close();

        } catch(Exception e) {

            System.out.println("Standalone Server");
        }

        return clusterList;
    }

// This will show the available space of disk in %  
    public List<DiskInfo> getDiskStatus() {

        List<DiskInfo> diskList =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            Statement stmt =
                    con.createStatement();

            String query =
                "SELECT DISTINCT " +
                "SUBSTRING(volume_mount_point,1,1) AS Drive, " +
                "total_bytes/1024/1024 AS TotalMB, " +
                "available_bytes/1024/1024 AS AvailableMB, " +
                "(available_bytes * 100.0 / total_bytes) AS FreePercent " +
                "FROM sys.master_files f " +
                "CROSS APPLY " +
                "sys.dm_os_volume_stats(f.database_id,f.file_id)";

            ResultSet rs =
                    stmt.executeQuery(query);

            while(rs.next()) {

                DiskInfo disk =
                        new DiskInfo();

                disk.setDrive(
                        rs.getString("Drive"));

                disk.setTotalMB(
                        rs.getLong("TotalMB"));

                disk.setFreeMB(
                        rs.getLong("AvailableMB"));

                disk.setFreePercent(
                        rs.getDouble("FreePercent"));

                diskList.add(disk);
            }

            con.close();

        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return diskList;
    }
 
    // This will show memory and CPU usage
    public ServerResourceInfo getServerResources() {

        ServerResourceInfo resource =
                new ServerResourceInfo();

        try {

            Connection con =
                    DBConnection.getConnection();

            Statement stmt =
                    con.createStatement();

            String memoryQuery =
                    "SELECT " +
                    "CAST(sm.total_physical_memory_kb/1024.0/1024 AS DECIMAL(10,2)) AS TotalServerRAMGB, " +
                    "CAST(CONVERT(INT, sc.value_in_use)/1024.0 AS DECIMAL(10,2)) AS SQLMaxMemoryGB, " +
                    "CAST(pm.physical_memory_in_use_kb/1024.0/1024 AS DECIMAL(10,2)) AS SQLMemoryUsedGB " +
                    "FROM sys.dm_os_process_memory pm " +
                    "CROSS JOIN sys.dm_os_sys_memory sm " +
                    "CROSS JOIN sys.configurations sc " +
                    "WHERE sc.name = 'max server memory (MB)'";

            ResultSet rs =
                    stmt.executeQuery(memoryQuery);

            if(rs.next()) {

                resource.setTotalServerRAMGB(
                        rs.getDouble("TotalServerRAMGB"));

                resource.setSqlMaxMemoryGB(
                        rs.getDouble("SQLMaxMemoryGB"));

                resource.setSqlMemoryUsedGB(
                        rs.getDouble("SQLMemoryUsedGB"));
            }

            String cpuQuery =
                    "SELECT TOP 1 " +
                    "record.value('(./Record/SchedulerMonitorEvent/SystemHealth/ProcessUtilization)[1]', 'int') AS SQLCPUUsage " +
                    "FROM ( " +
                    "SELECT CAST(record AS XML) AS record " +
                    "FROM sys.dm_os_ring_buffers " +
                    "WHERE ring_buffer_type = 'RING_BUFFER_SCHEDULER_MONITOR' " +
                    "AND record LIKE '%<SystemHealth>%' " +
                    ") x";

            ResultSet cpuRs =
                    stmt.executeQuery(cpuQuery);

            if(cpuRs.next()) {

                resource.setSqlCpuUsage(
                        cpuRs.getInt("SQLCPUUsage"));
            }

            cpuRs.close();
            rs.close();
            stmt.close();
            con.close();

        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return resource;
    }
    
  // Job status  
    public List<JobStatus> getCriticalJobs() {

        List<JobStatus> jobs = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            Statement stmt = con.createStatement();

            String query =
                    "WITH LatestJobRun AS ( " +
                    "SELECT j.name, " +
                    "h.run_status, " +
                    "h.run_date, " +
                    "h.run_time, " +
                    "ROW_NUMBER() OVER " +
                    "(PARTITION BY j.job_id ORDER BY h.instance_id DESC) rn " +
                    "FROM msdb.dbo.sysjobs j " +
                    "INNER JOIN msdb.dbo.sysjobhistory h " +
                    "ON j.job_id = h.job_id " +
                    "WHERE h.step_id = 0 " +
                    ") " +
                    "SELECT * " +
                    "FROM LatestJobRun " +
                    "WHERE rn = 1 " +
                    "AND ( " +
                    "name LIKE '%Backup%' " +
                    "OR name LIKE '%Log%' " +
                    "OR name LIKE '%Index%' " +
                    ")";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
            	
            	System.out.println(
            		    rs.getString("name")
            		);
                JobStatus job = new JobStatus();

                job.setJobName(rs.getString("name"));

                String status;

                switch (rs.getInt("run_status")) {

                case 0:
                    status = "Failed";
                    break;

                case 1:
                    status = "Succeeded";
                    break;

                case 2:
                    status = "Retry";
                    break;

                case 3:
                    status = "Cancelled";
                    break;

                case 4:
                    status = "Running";
                    break;

                default:
                    status = "Unknown";
                }

                job.setStatus(status);

                String runDate =
                        String.valueOf(rs.getInt("run_date"));

                String runTime =
                        String.format("%06d",
                        rs.getInt("run_time"));

                String lastRun =
                        runDate.substring(0, 4) + "-" +
                        runDate.substring(4, 6) + "-" +
                        runDate.substring(6, 8) + " " +
                        runTime.substring(0, 2) + ":" +
                        runTime.substring(2, 4) + ":" +
                        runTime.substring(4, 6);

                job.setLastRunDate(lastRun);

                jobs.add(job);
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jobs;
    }
    */

	//🌟   connection sql server inventory
    
    public List<ServerInventory> getServers() {

        List<ServerInventory> servers =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            Statement stmt =
                    con.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                    "SELECT " +
                    "ServerID, " +
                    "ServerName, " +
                    "InstanceName, " +
                    "PortNumber, " +
                    "Environment, " +
                    "Site, " +
                    "IsActive " +
                    "FROM SQL_DETAILS.dbo.ServerInventory " +
                    "WHERE IsActive = 1");

            while(rs.next()) {

                ServerInventory server =
                        new ServerInventory();

                server.setServerId(
                        rs.getInt("ServerID"));

                server.setServerName(
                        rs.getString("ServerName"));

                server.setInstanceName(
                        rs.getString("InstanceName"));

                server.setPortNumber(
                        rs.getInt("PortNumber"));

                server.setEnvironment(
                        rs.getString("Environment"));

                server.setActive(
                        rs.getBoolean("IsActive"));
                
                server.setSite(
                        rs.getString("Site"));

                servers.add(server);
            }

            con.close();

        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return servers;
    }
   
//    Checking server connection
    public List<ServerConnectionStatus> testConnections() {

        List<ServerConnectionStatus> results =
                new ArrayList<>();

        List<ServerInventory> servers =
                getServers();

        for(ServerInventory srv : servers) {

            ServerConnectionStatus status =
                    new ServerConnectionStatus();

            status.setServerName(
                    srv.getServerName());

            status.setInstanceName(
                    srv.getInstanceName());

            status.setServerId(
                    srv.getServerId());

            status.setEnvironment(
                    srv.getEnvironment());

            System.out.println(
                "Inventory Data -> "
                + "Server="
                + srv.getServerName()
                + ", Instance="
                + srv.getInstanceName()
                + ", Port="
                + srv.getPortNumber());

            try {

                String url =
                    "jdbc:sqlserver://"
                    + srv.getServerName()
                    + ":"
                    + srv.getPortNumber()
                    + ";databaseName=master;"
                    + "encrypt=true;"
                    + "trustServerCertificate=true;";

                System.out.println(
                    "Trying URL -> "
                    + url);

                Connection con =
                	    DriverManager.getConnection(
                	        url,
                	        appConfig.getUsername(),
                	        appConfig.getPassword());

                status.setStatus("Connected");

                con.close();

            }
            catch(Exception e) {

                status.setStatus("Failed");

                System.out.println(
                    "Failed -> "
                    + srv.getServerName()
                    + "\\"
                    + srv.getInstanceName()
                    + " : "
                    + e.getMessage());
            }

            System.out.println(
                "Result -> "
                + "ServerId=" + status.getServerId()
                + ", ServerName=" + status.getServerName()
                + ", InstanceName=" + status.getInstanceName()
                + ", Status=" + status.getStatus());

            results.add(status);
        }

        return results;
    }

    
	//   creating new dashboard   
    public ServerInventory getServerById(int serverId) {

        ServerInventory server =
                new ServerInventory();

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(
                            "SELECT * " +
                            "FROM SQL_DETAILS.dbo.ServerInventory " +
                            "WHERE ServerID = ? " +
                            "AND IsActive = 1");

            ps.setInt(1, serverId);

            System.out.println(
                    "Looking for ServerID = "
                    + serverId);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {

                System.out.println(
                        "Found Server -> "
                        + rs.getString("ServerName")
                        + "\\"
                        + rs.getString("InstanceName")
                        + ":"
                        + rs.getInt("PortNumber"));

                server.setServerId(
                        rs.getInt("ServerID"));

                server.setServerName(
                        rs.getString("ServerName"));

                server.setInstanceName(
                        rs.getString("InstanceName"));

                server.setPortNumber(
                        rs.getInt("PortNumber"));

                server.setEnvironment(
                        rs.getString("Environment"));

                server.setActive(
                        rs.getBoolean("IsActive"));

                server.setSite(
                        rs.getString("Site"));
            }
            else {

                System.out.println(
                        "No server found for ServerID = "
                        + serverId);
            }

            con.close();
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return server;
    }
    
 // Dynamic Connection  
    public Connection getTargetConnection(
            ServerInventory server)
    throws Exception {

        return DBConnection.getConnection(
                server.getServerName(),
                server.getPortNumber());
    }
    
// Server Name and version details    
    public HealthReport getHealthReport(Connection con) {

        HealthReport report = new HealthReport();

        try {

            Statement stmt =
                    con.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT @@SERVERNAME AS ServerName, "
                          + "@@VERSION AS Version");

            if(rs.next()) {

                report.setServerName(
                        rs.getString("ServerName"));

                report.setVersion(
                        rs.getString("Version"));
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return report;
    }
    
//    Reading server details from serverInventory table of SQL DETAILS.
    
    public ServerInventory getServerByName(String serverName) {

        ServerInventory server =
                new ServerInventory();

        try {

            Connection con =
                    DBConnection.getConnection();

            PreparedStatement ps =
                    con.prepareStatement(
                        "SELECT * " +
                        "FROM SQL_DETAILS.dbo.ServerInventory " +
                        "WHERE ServerName = ?");

            ps.setString(1, serverName);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()) {

                server.setServerName(
                        rs.getString("ServerName"));

                server.setInstanceName(
                        rs.getString("InstanceName"));

                server.setPortNumber(
                        rs.getInt("PortNumber"));

                server.setEnvironment(
                        rs.getString("Environment"));
                
                server.setSite(
                        rs.getString("Site"));
            }

            con.close();

        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return server;
    }
    
//    Getting server resource details (Memory CPU)
    public ServerResourceInfo getServerResources(
            Connection con) {

        ServerResourceInfo resource =
                new ServerResourceInfo();

        try {

            Statement stmt =
                    con.createStatement();

            String memoryQuery =
                    "SELECT " +
                    "CAST(sm.total_physical_memory_kb/1024.0/1024 AS DECIMAL(10,2)) AS TotalServerRAMGB, " +

                    "CAST(((sm.total_physical_memory_kb - sm.available_physical_memory_kb) * 100.0 / sm.total_physical_memory_kb) AS DECIMAL(10,2)) AS ServerMemoryUtilization, " +

                    "CAST(CONVERT(INT, sc.value_in_use)/1024.0 AS DECIMAL(10,2)) AS SQLMaxMemoryGB, " +

                    "CAST(pm.physical_memory_in_use_kb/1024.0/1024 AS DECIMAL(10,2)) AS SQLMemoryUsedGB " +

                    "FROM sys.dm_os_process_memory pm " +
                    "CROSS JOIN sys.dm_os_sys_memory sm " +
                    "CROSS JOIN sys.configurations sc " +
                    "WHERE sc.name = 'max server memory (MB)'";

            ResultSet rs =
                    stmt.executeQuery(memoryQuery);

            if(rs.next()) {

                resource.setTotalServerRAMGB(
                        rs.getDouble("TotalServerRAMGB"));

                resource.setServerMemoryUtilization(
                        rs.getDouble("ServerMemoryUtilization"));

                resource.setSqlMaxMemoryGB(
                        rs.getDouble("SQLMaxMemoryGB"));

                resource.setSqlMemoryUsedGB(
                        rs.getDouble("SQLMemoryUsedGB"));
            }

            String cpuQuery =
                    "SELECT TOP 1 " +
                    "record.value('(./Record/SchedulerMonitorEvent/SystemHealth/SystemIdle)[1]', 'int') AS SystemIdle, " +
                    "record.value('(./Record/SchedulerMonitorEvent/SystemHealth/ProcessUtilization)[1]', 'int') AS SQLCPUUsage " +
                    "FROM ( " +
                    "SELECT CAST(record AS XML) AS record " +
                    "FROM sys.dm_os_ring_buffers " +
                    "WHERE ring_buffer_type = 'RING_BUFFER_SCHEDULER_MONITOR' " +
                    "AND record LIKE '%<SystemHealth>%' " +
                    ") x";

            ResultSet cpuRs =
                    stmt.executeQuery(cpuQuery);

            if(cpuRs.next()) {

                int sqlCpu =
                        cpuRs.getInt("SQLCPUUsage");

                int systemIdle =
                        cpuRs.getInt("SystemIdle");

                int totalCpu =
                        100 - systemIdle;

                resource.setSqlCpuUsage(sqlCpu);

                resource.setTotalCpuUsage(totalCpu);
            }

        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return resource;
    }
//  DB STATUS OF SERVER 
    public List<DatabaseInfo> getDatabases(
            Connection con) {

        List<DatabaseInfo> databases =
                new ArrayList<>();

        try {

            Statement stmt =
                    con.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                    "SELECT " +
                    "d.name, " +
                    "d.state_desc, " +
                    "MAX(bs.backup_finish_date) AS LastBackupDate " +
                    "FROM sys.databases d " +
                    "LEFT JOIN msdb.dbo.backupset bs " +
                    "ON bs.database_name = d.name " +
                    "AND bs.type = 'D' " +
                    "GROUP BY d.name,d.state_desc " +
                    "ORDER BY d.name");
            

            while(rs.next()) {

                DatabaseInfo db =
                        new DatabaseInfo();

                db.setName(
                        rs.getString("name"));

                db.setStatus(
                        rs.getString("state_desc"));

                Timestamp backupTime =
                        rs.getTimestamp("LastBackupDate");

                if(backupTime != null) {

                    db.setLastBackupDate(
                            backupTime.toString());

                    long hours =
                            Duration.between(
                                    backupTime.toLocalDateTime(),
                                    LocalDateTime.now())
                                    .toHours();

                    if(hours <= 168) {

                        db.setBackupStatusColor(
                                "success");

                    }
//                    else if(hours <= 48) {
//
//                        db.setBackupStatusColor(
//                                "warning");
//
//                    }
                    else {

                        db.setBackupStatusColor(
                                "danger");
                    }
                }
                else {

                    db.setLastBackupDate("N/A");

                    db.setBackupStatusColor(
                            "secondary");
                }
                databases.add(db);   
            }

        } catch(Exception e) {

            e.printStackTrace();
        }

        return databases;
    }
// Critical Jobs
    public List<JobStatus> getCriticalJobs(
            Connection con) {

        List<JobStatus> jobs =
                new ArrayList<>();

        try {

            Statement stmt =
                    con.createStatement();

            String query =
                    "WITH LatestJobRun AS ( " +
                    "SELECT " +
                    "j.job_id, " +
                    "j.name, " +
                    "h.run_status, " +
                    "h.run_date, " +
                    "h.run_time, " +
                    "ROW_NUMBER() OVER " +
                    "(PARTITION BY j.job_id ORDER BY h.instance_id DESC) rn " +
                    "FROM msdb.dbo.sysjobs j " +
                    "INNER JOIN msdb.dbo.sysjobhistory h " +
                    "ON j.job_id = h.job_id " +
                    "WHERE h.step_id = 0 " +
                    ") " +

                    "SELECT " +
                    "ljr.name, " +
                    "ljr.run_status, " +
                    "ljr.run_date, " +
                    "ljr.run_time, " +

                    "CASE " +
                    "WHEN ja.start_execution_date IS NOT NULL " +
                    "AND ja.stop_execution_date IS NULL " +
                    "THEN 'Running' " +
                    "ELSE 'Not Running' " +
                    "END AS CurrentStatus " +

                    "FROM LatestJobRun ljr " +

                    "LEFT JOIN msdb.dbo.sysjobactivity ja " +
                    "ON ljr.job_id = ja.job_id " +
                    "AND ja.session_id = " +
                    "(SELECT MAX(session_id) " +
                    "FROM msdb.dbo.sysjobactivity) " +

                    "WHERE ljr.rn = 1 " +
                    "AND ( " +
                    "ljr.name LIKE '%Backup%' " +
                    "OR ljr.name LIKE '%Log%' " +
                    "OR ljr.name LIKE '%Index%' " +
                    "OR ljr.name LIKE '%Integrity%' " +
                    ") " +
                    "ORDER BY ljr.name";

            ResultSet rs =
                    stmt.executeQuery(query);

            while(rs.next()) {

                JobStatus job =
                        new JobStatus();

                job.setJobName(
                        rs.getString("name"));
                
                job.setCurrentStatus(
                        rs.getString("CurrentStatus"));

                String status;

                switch(rs.getInt("run_status")) {

                    case 0:
                        status = "Failed";
                        break;

                    case 1:
                        status = "Succeeded";
                        break;

                    case 2:
                        status = "Retry";
                        break;

                    case 3:
                        status = "Cancelled";
                        break;

                    case 4:
                        status = "Running";
                        break;

                    default:
                        status = "Unknown";
                }

                job.setStatus(status);

                String runDate =
                        String.valueOf(
                                rs.getInt("run_date"));

                String runTime =
                        String.format(
                                "%06d",
                                rs.getInt("run_time"));

                String lastRun =
                        runDate.substring(0,4)
                        + "-"
                        + runDate.substring(4,6)
                        + "-"
                        + runDate.substring(6,8)
                        + " "
                        + runTime.substring(0,2)
                        + ":"
                        + runTime.substring(2,4)
                        + ":"
                        + runTime.substring(4,6);

                job.setLastRunDate(lastRun);

                jobs.add(job);
            }

        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return jobs;
    }
//    Getting Disk Status
    public List<DiskInfo> getDiskStatus(
            Connection con) {

        List<DiskInfo> diskList =
                new ArrayList<>();

        try {

            System.out.println(
                    "Executing Disk Query...");

            Statement stmt =
                    con.createStatement();

            String query =
                    "SELECT DISTINCT " +
                    "volume_mount_point, " +
                    "CAST(total_bytes/1024.0/1024/1024 AS DECIMAL(10,2)) AS TotalGB, " +
                    "CAST(available_bytes/1024.0/1024/1024 AS DECIMAL(10,2)) AS FreeGB, " +
                    "(available_bytes * 100.0 / total_bytes) AS FreePercent " +
                    "FROM sys.master_files f " +
                    "CROSS APPLY sys.dm_os_volume_stats(" +
                    "f.database_id, f.file_id)";

            ResultSet rs =
                    stmt.executeQuery(query);

            while(rs.next()) {

                DiskInfo disk =
                        new DiskInfo();

                String volume =
                        rs.getString(
                                "volume_mount_point");

                disk.setDrive(volume);

                disk.setTotalGB(
                        rs.getDouble("TotalGB"));

                disk.setFreeGB(
                        rs.getDouble("FreeGB"));

                double freePercent =
                        rs.getDouble("FreePercent");

                disk.setFreePercent(
                        freePercent);

                if(freePercent < 10) {

                    disk.setDiskStatusColor(
                            "danger");

                }
                else if(freePercent < 20) {

                    disk.setDiskStatusColor(
                            "warning");

                }
                else {

                    disk.setDiskStatusColor(
                            "success");
                }

                System.out.println(
                        "Drive="
                        + volume
                        + ", Free="
                        + freePercent);

                diskList.add(disk);
            }
           }
            
        catch(Exception e) {

            System.out.println(
                    "Disk Query Error");

            e.printStackTrace();
        }

        return diskList;
    }
//  Getting AGstatus  
    public List<AGInfo> getAGStatus(
            Connection con) {

        List<AGInfo> agList =
                new ArrayList<>();

        try {

            Statement stmt =
                    con.createStatement();

            String query =
                    "SELECT " +
                    "ag.name AS AGName, " +
                    "ar.replica_server_name AS ReplicaServer, " +
                    "ars.role_desc, " +
                    "ars.synchronization_health_desc " +
                    "FROM sys.dm_hadr_availability_replica_states ars " +
                    "INNER JOIN sys.availability_replicas ar " +
                    "ON ars.replica_id = ar.replica_id " +
                    "INNER JOIN sys.availability_groups ag " +
                    "ON ars.group_id = ag.group_id " +
                    "ORDER BY ag.name, ar.replica_server_name";

            ResultSet rs =
                    stmt.executeQuery(query);

            while(rs.next()) {

                AGInfo ag =
                        new AGInfo();

                ag.setAgName(
                        rs.getString("AGName"));

                ag.setReplicaServer(
                        rs.getString("ReplicaServer"));

                ag.setRole(
                        rs.getString("role_desc"));

                ag.setSyncStatus(
                        rs.getString(
                                "synchronization_health_desc"));

                agList.add(ag);
            }

        }
        catch(Exception e) {

            System.out.println(
                    "AG Not Configured");
        }

        return agList;
    }

// cluster dynamic method   
    public List<ClusterInfo> getClusterStatus(
            Connection con) {

        List<ClusterInfo> clusterList =
                new ArrayList<>();

        try {

            Statement stmt =
                    con.createStatement();

            // First try AG cluster members
            ResultSet rs =
                    stmt.executeQuery(
                            "SELECT " +
                            "member_name, " +
                            "member_state_desc, " +
                            "member_type_desc " +
                            "FROM sys.dm_hadr_cluster_members");

            while(rs.next()) {

                ClusterInfo node =
                        new ClusterInfo();

                node.setNodeName(
                        rs.getString("member_name"));

                node.setNodeStatus(
                        rs.getString("member_state_desc"));

                node.setNodeType(
                        rs.getString("member_type_desc"));

                clusterList.add(node);
            }

            // If no AG cluster members found
            if(clusterList.isEmpty()) {

                rs = stmt.executeQuery(
                        "SELECT " +
                        "CAST(SERVERPROPERTY('IsClustered') AS INT)");

                rs.next();

                int isClustered =
                        rs.getInt(1);

                if(isClustered == 1) {

                    rs = stmt.executeQuery(
                            "SELECT " +
                            "NodeName, " +
                            "status_description " +
                            "FROM sys.dm_os_cluster_nodes");

                    while(rs.next()) {

                        ClusterInfo node =
                                new ClusterInfo();

                        node.setNodeName(
                                rs.getString("NodeName"));

                        node.setNodeStatus(
                                rs.getString(
                                        "status_description"));

                        node.setNodeType(
                                "CLUSTER_NODE");

                        clusterList.add(node);
                    }
                }
                else {

                    ClusterInfo node =
                            new ClusterInfo();

                    node.setNodeName("N/A");
                    node.setNodeType("STANDALONE");
                    node.setNodeStatus("NOT CLUSTERED");

                    clusterList.add(node);
                }
            }
        }
        catch(Exception e) {

            System.out.println(
                    "Cluster Query Failed : "
                    + e.getMessage());

            e.printStackTrace();
        }

        return clusterList;
    }

//  Blocking session
    
    public List<BlockingInfo> getBlockingSummary(
            Connection con) {

        List<BlockingInfo> blockingList =
                new ArrayList<>();

        try {

            Statement stmt =
                    con.createStatement();

            String query =
                    "SELECT " +
                    "r.session_id AS BlockedSPID, " +
                    "r.blocking_session_id AS BlockingSPID, " +
                    "DB_NAME(r.database_id) AS DatabaseName, " +
                    "LEFT(ISNULL(s.program_name,''),20) AS ProgramName, " +
                    "r.wait_time / 1000 AS WaitSeconds, " +
                    "LEFT(" +
                    "REPLACE(" +
                    "REPLACE(" +
                    "ISNULL(txt.text,''), " +
                    "CHAR(13),' '), " +
                    "CHAR(10),' '), " +
                    "20) + '...' AS CommandText " +
                    "FROM sys.dm_exec_requests r " +
                    "INNER JOIN sys.dm_exec_sessions s " +
                    "ON r.session_id = s.session_id " +
                    "CROSS APPLY sys.dm_exec_sql_text(r.sql_handle) txt " +
                    "WHERE r.blocking_session_id <> 0 " +
                    "ORDER BY r.wait_time DESC";

            ResultSet rs =
                    stmt.executeQuery(query);

            while(rs.next()) {

                BlockingInfo block =
                        new BlockingInfo();

                block.setBlockedSpid(
                        rs.getInt(
                                "BlockedSPID"));

                block.setBlockingSpid(
                        rs.getInt(
                                "BlockingSPID"));

                block.setDatabaseName(
                        rs.getString(
                                "DatabaseName"));

                block.setProgramName(
                        rs.getString(
                                "ProgramName"));

                int wait =
                        rs.getInt(
                                "WaitSeconds");

                block.setWaitSeconds(
                        wait);

                block.setCommandText(
                        rs.getString(
                                "CommandText"));

                if(wait > 300) {

                    block.setWaitColor(
                            "danger");

                }
                else if(wait > 60) {

                    block.setWaitColor(
                            "warning");

                }
                else {

                    block.setWaitColor(
                            "success");
                }

                blockingList.add(
                        block);
            }

        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return blockingList;
    }
    
    
    
//  error log
    
    public List<ErrorLogInfo> getCriticalErrorLogs(
            Connection con) {

        List<ErrorLogInfo> errorList =
                new ArrayList<>();

        try {

            Statement stmt =
                    con.createStatement();

            ResultSet rs =
                    stmt.executeQuery(
                    "EXEC xp_readerrorlog 0, 1");
            
            LocalDateTime cutoff =
                    LocalDateTime.now()
                                 .minusHours(24);

            while(rs.next()) {
            
                Timestamp errorTime =
                        rs.getTimestamp(1);

                if(errorTime == null) {
                    continue;
                }

                if(errorTime.toLocalDateTime()
                             .isBefore(cutoff)) {

                    continue;
                }
                
                String logDate =
                        rs.getString(1);

//                String processInfo =
//                        rs.getString(2);

                String message =
                        rs.getString(3);

                if(message == null) {
                    continue;
                }

                String upperMessage =
                        message.toUpperCase();
                
//                System.out.println(message);
                
                ErrorLogInfo log = null;
                if(upperMessage.contains(
                        "THIS IS AN INFORMATIONAL MESSAGE ONLY")|| upperMessage.contains(
                                "SUCCESSFULL")) {

                    continue;
                }



                else if(upperMessage.contains("SUSPECT")) {

                    log = new ErrorLogInfo();
                    log.setCategory("Database State");
                    log.setSeverity("Critical");

                }
                else if(upperMessage.contains("RECOVERY_PENDING")) {

                    log = new ErrorLogInfo();
                    log.setCategory("Database State");
                    log.setSeverity("Critical");

                }
                else if(upperMessage.contains("EMERGENCY")) {

                    log = new ErrorLogInfo();
                    log.setCategory("Database State");
                    log.setSeverity("Critical");

                }
                else if(upperMessage.contains("823")
                        || upperMessage.contains("824")
                        || upperMessage.contains("825")) {

                    log = new ErrorLogInfo();
                    log.setCategory("I/O Error");
                    log.setSeverity("Critical");

                }
                else if(upperMessage.contains("NON-YIELDING")) {

                    log = new ErrorLogInfo();
                    log.setCategory("System Health");
                    log.setSeverity("Critical");

                }
                else if(upperMessage.contains("OUT OF MEMORY")
                        || upperMessage.contains("INSUFFICIENT MEMORY")) {

                    log = new ErrorLogInfo();
                    log.setCategory("Memory");
                    log.setSeverity("Critical");

                }
                else if(upperMessage.contains("BACKUP")
                        && upperMessage.contains("FAILED")) {

                    log = new ErrorLogInfo();
                    log.setCategory("Backup Failure");
                    log.setSeverity("High");

                }
                else if(upperMessage.contains("LOGIN FAILED")) {

                    log = new ErrorLogInfo();
                    log.setCategory("lOGIN Failure");
                    log.setSeverity("Medium");

                }

                if(log != null) {

                    log.setLogDate(logDate);
                    log.setMessage(message);

                    errorList.add(log);
                }
            }

        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return errorList;
    }
    
    
    // Site wise page
    
 // Site wise page

    public List<SiteSummary> getSites() {

        List<SiteSummary> sites =
                new ArrayList<>();

        try(Connection con =
                DBConnection.getConnection()) {

            String query =

                "SELECT " +
                "Site, " +

                "COUNT(*) AS TotalServers, " +

                "SUM(CASE " +
                "WHEN HealthStatus = 'Healthy' " +
                "THEN 1 ELSE 0 END) AS HealthyCount, " +

                "SUM(CASE " +
                "WHEN HealthStatus = 'Warning' " +
                "THEN 1 ELSE 0 END) AS WarningCount, " +

                "SUM(CASE " +
                "WHEN HealthStatus = 'Critical' " +
                "THEN 1 ELSE 0 END) AS CriticalCount, " +

                "SUM(CASE " +
                "WHEN ConnectionStatus = 'Failed' " +
                "THEN 1 ELSE 0 END) AS FailedCount " +

                "FROM dbo.ServerHealthStatus " +

                "GROUP BY Site " +

                "ORDER BY Site";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()) {

                SiteSummary site =
                        new SiteSummary();

                site.setSite(
                        rs.getString("Site"));

                site.setTotalServers(
                        rs.getInt("TotalServers"));

                site.setHealthyCount(
                        rs.getInt("HealthyCount"));

                site.setWarningCount(
                        rs.getInt("WarningCount"));

                site.setCriticalCount(
                        rs.getInt("CriticalCount"));

                site.setFailedCount(
                        rs.getInt("FailedCount"));

                sites.add(site);
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return sites;
    }
    
    public List<ServerConnectionStatus>getServersBySite(String site) {

        List<ServerConnectionStatus> results =
                new ArrayList<>();

        List<ServerInventory> servers =
                getServers()
                .stream()
                .filter(x ->
                    x.getSite() != null
                    && x.getSite().trim()
                        .equalsIgnoreCase(site.trim()))
                .toList();

        System.out.println(
                "Selected Site = "
                + site);

        System.out.println(
                "Servers Found = "
                + servers.size());

        for(ServerInventory srv : servers) {

            System.out.println(
                    "Server="
                    + srv.getServerName()
                    + ", Site="
                    + srv.getSite());

            ServerConnectionStatus status =
                    new ServerConnectionStatus();

            status.setServerId(
                    srv.getServerId());

            status.setServerName(
                    srv.getServerName());

            status.setEnvironment(
                    srv.getEnvironment());

            status.setInstanceName(
                    srv.getInstanceName());

            try {

                Connection con =
                        getTargetConnection(srv);

                status.setStatus(
                        "Connected");

                HealthResult result =
                        calculateHealthStatus(con);

                status.setHealthStatus(
                        result.getHealthStatus());

                status.setHealthColor(
                        getHealthColor(
                                result.getHealthStatus()));
                
                status.setLastIssue(
                        result.getLastIssue());

                con.close();
            }
            catch(Exception e) {

                status.setStatus(
                        "Failed");

                status.setHealthStatus(
                        "N/A");

                status.setHealthColor(
                        "secondary");
                
                status.setLastIssue(
                        "Connection Failed");
            }
            results.add(status);
        }

        return results;
    }
    
    
    
    private HealthResult calculateHealthStatus(
            Connection con) {

        HealthResult result =
                new HealthResult();

        Set<String> issues =
                new LinkedHashSet<>();

        try {

            boolean warning = false;
            boolean critical = false;

            /*
             * DATABASE STATUS
             */

            List<DatabaseInfo> databases =
                    getDatabases(con);

            for(DatabaseInfo db : databases) {

                String status =
                        db.getStatus();

                if(status == null) {
                    continue;
                }

                if(!"ONLINE".equalsIgnoreCase(status)) {

                    critical = true;

                    issues.add(
                            "Database State");
                }
            }

            /*
             * DISK STATUS
             */

            List<DiskInfo> disks =
                    getDiskStatus(con);

            for(DiskInfo disk : disks) {

                if(disk.getFreePercent() < 10) {

                    critical = true;

                    issues.add(
                            "Disk Space");
                }
                else if(disk.getFreePercent() < 20) {

                    warning = true;

                    issues.add(
                            "Disk Space");
                }
            }

            /*
             * BLOCKING
             */

            List<BlockingInfo> blocks =
                    getBlockingSummary(con);

            for(BlockingInfo block : blocks) {

                if(block.getWaitSeconds() > 300) {

                    critical = true;

                    issues.add(
                            "Blocking");
                }
                else if(block.getWaitSeconds() > 0) {

                    warning = true;

                    issues.add(
                            "Blocking");
                }
            }

            /*
             * ERROR LOGS
             */

            List<ErrorLogInfo> logs =
                    getCriticalErrorLogs(con);

            for(ErrorLogInfo log : logs) {

                String category =
                        log.getCategory();

                if(category == null) {
                    continue;
                }

                if(category.equalsIgnoreCase("I/O Error")) {

                	warning  = true;

                    issues.add(
                            "I/O Error");
                }
                else if(category.equalsIgnoreCase(
                        "Database State")) {

                	warning = true;

                    issues.add(
                            "Database State");
                }
                else if(category.equalsIgnoreCase(
                        "Login Failure")) {

                    warning = true;

                    issues.add(
                            "Login Failures");
                }
                else {

                    warning = true;

                    issues.add(
                            "Error Logs");
                }
            }

            /*
             * JOBS
             */

            List<JobStatus> jobs =
                    getCriticalJobs(con);

            for(JobStatus job : jobs) {

                if("Failed".equalsIgnoreCase(
                        job.getStatus())) {

                    warning = true;

                    issues.add(
                            "Failed Jobs");
                }
            }

            /*
             * FINAL STATUS
             */

            if(critical) {

                result.setHealthStatus(
                        "Critical");
            }
            else if(warning) {

                result.setHealthStatus(
                        "Warning");
            }
            else {

                result.setHealthStatus(
                        "Healthy");

                issues.add(
                        "No Issues");
            }

            result.setLastIssue(
                    String.join(
                            ", ",
                            issues));

            return result;
        }
        catch(Exception e) {

            result.setHealthStatus(
                    "Critical");

            result.setLastIssue(
                    "Health Check Failed");

            return result;
        }
    }  
    
    private String getHealthColor(String healthStatus) {

        switch(healthStatus) {

            case "Healthy":
                return "success";

            case "Warning":
                return "warning";

            case "Critical":
                return "danger";

            case "N/A":
                return "secondary";

            default:
                return "secondary";
        }
    }
    
 // Scheduling the refresh rate

    @Scheduled(cron = "0 0 1 * * ?", zone = "Europe/London") // Refresh every morning 6 AM
    public void refreshServerHealth() {

        if(refreshRunning) {

            System.out.println(
                    "Refresh already running.");

            return;
        }

        try {

            refreshRunning = true;

            System.out.println(
                    "Health Refresh Started");

            List<ServerInventory> servers =
                    getServers();

            for(ServerInventory server : servers) {

                refreshServerStatus(server);
            }

            System.out.println(
                    "Health Refresh Completed");
        }
        finally {

            refreshRunning = false;
        }
    }


    // Refreshing the servers

    private void refreshServerStatus(
            ServerInventory server) {

        String connectionStatus;
        String healthStatus;
        String lastIssue;

        try {

            Connection con =
                    getTargetConnection(server);

            connectionStatus =
                    "Connected";

            HealthResult result =
                    calculateHealthStatus(con);

            healthStatus =
                    result.getHealthStatus();

            lastIssue =
                    result.getLastIssue();

            con.close();
        }
        catch(Exception e) {

            connectionStatus =
                    "Failed";

            healthStatus =
                    "N/A";

            lastIssue =
                    "Connection Failed";
        }

        saveHealthStatus(
                server,
                connectionStatus,
                healthStatus,
                lastIssue);

        System.out.println(
                server.getServerName()
                + " '\' "
                +server.getInstanceName()
                +":"
                + server.getPortNumber()
                + " -> "
                + connectionStatus
                + " -> "
                + healthStatus
                + " -> "
                + lastIssue);
    }
    
    // Inserting refresh status into table.
    
    private void saveHealthStatus(
            ServerInventory server,
            String connectionStatus,
            String healthStatus,
            String lastIssue) {

        try (Connection con =
                DBConnection.getConnection()) {

            String query =

                "MERGE dbo.ServerHealthStatus AS T " +

                "USING (SELECT ? AS ServerID) AS S " +

                "ON T.ServerID = S.ServerID " +

                "WHEN MATCHED THEN " +

                "UPDATE SET " +
                "ServerName = ?, " +
                "InstanceName =?," +
                "Site = ?, " +
                "Environment = ?, " +
                "ConnectionStatus = ?, " +
                "HealthStatus = ?, " +
                "LastIssue = ?, " +
                "LastRefreshTime = GETDATE() " +

                "WHEN NOT MATCHED THEN " +

                "INSERT ( " +
                "ServerID, " +
                "ServerName, " +
                "InstanceName,"+
                "Site, " +
                "Environment, " +
                "ConnectionStatus, " +
                "HealthStatus, " +
                "LastIssue, " +
                "LastRefreshTime " +
                ") " +

				"VALUES ( " +
				"?, ?, ?, ?, ?, ?, ?, ?, GETDATE() " +
				");";;

            PreparedStatement ps =
                    con.prepareStatement(query);

            /*
             * UPDATE SECTION
             */

            ps.setInt(1,
                    server.getServerId());

            ps.setString(2,
                    server.getServerName());

            ps.setString(3,
                    server.getInstanceName());

            ps.setString(4,
                    server.getSite());

            ps.setString(5,
                    server.getEnvironment());

            ps.setString(6,
                    connectionStatus);

            ps.setString(7,
                    healthStatus);

            ps.setString(8,
                    lastIssue);

            /*
             * INSERT SECTION
             */

            ps.setInt(9,
                    server.getServerId());

            ps.setString(10,
                    server.getServerName());

            ps.setString(11,
                    server.getInstanceName());

            ps.setString(12,
                    server.getSite());

            ps.setString(13,
                    server.getEnvironment());

            ps.setString(14,
                    connectionStatus);

            ps.setString(15,
                    healthStatus);

            ps.setString(16,
                    lastIssue);

            System.out.println(
                    "Saving -> "
                    + server.getServerName()
                    + " | "                  
                    + server.getInstanceName()
                    + " | "
                    + connectionStatus
                    + " | "
                    + healthStatus
                    + " | "
                    + lastIssue);

            ps.executeUpdate();
        }
        catch(Exception e) {

            e.printStackTrace();
        }
    }
    
    
    
    // Displaying the stored table data in interface
    
    
    public List<ServerHealthStatus> getServerHealthBySite(
            String site) {

        List<ServerHealthStatus> results =
                new ArrayList<>();

        try(Connection con =
                DBConnection.getConnection()) {

            String query =

                "SELECT * " +
                "FROM dbo.ServerHealthStatus " +
                "WHERE Site = ? " +
                "ORDER BY ServerName";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ps.setString(1, site);

            ResultSet rs =
                    ps.executeQuery();

            while(rs.next()) {

                ServerHealthStatus status =
                        new ServerHealthStatus();

                status.setServerId(
                        rs.getInt("ServerID"));

                status.setServerName(
                        rs.getString("ServerName"));
                
                status.setInstanceName(
                        rs.getString("InstanceName"));
                

                status.setSite(
                        rs.getString("Site"));

                status.setEnvironment(
                        rs.getString("Environment"));

                status.setConnectionStatus(
                        rs.getString("ConnectionStatus"));

                status.setHealthStatus(
                        rs.getString("HealthStatus"));

                status.setLastIssue(
                        rs.getString("LastIssue"));

                Timestamp refreshTime =
                        rs.getTimestamp("LastRefreshTime");

                status.setLastRefreshTime(
                        refreshTime);

                if(refreshTime != null) {

                    status.setLastRefreshTimeDisplay(
                            refreshTime
                            .toLocalDateTime()
                            .format(
                                DateTimeFormatter.ofPattern(
                                    "dd-MMM-yyyy HH:mm:ss")));
                }

                results.add(status);
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return results;
    }

    
    
    public String getLastRefreshTime() {

        try(Connection con =
                DBConnection.getConnection()) {

            String query =
                    "SELECT MAX(LastRefreshTime) AS LastRefreshTime "
                  + "FROM dbo.ServerHealthStatus";

            PreparedStatement ps =
                    con.prepareStatement(query);

            ResultSet rs =
                    ps.executeQuery();

            if(rs.next()
                    && rs.getTimestamp("LastRefreshTime") != null) {

                return rs.getTimestamp("LastRefreshTime")
                        .toLocalDateTime()
                        .format(
                            DateTimeFormatter.ofPattern(
                                "dd-MMM-yyyy HH:mm:ss"));
            }
        }
        catch(Exception e) {

            e.printStackTrace();
        }

        return "Not Available";
    }
    

}