//package com.healthcheck;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//
//@Controller
//public class HealthController {
//
//    @Autowired
//    private SqlHealthService service;
//
//    @GetMapping("/")
//    public String home(Model model) {
//
//        model.addAttribute(
//                "report",
//                service.getHealthReport());
//
//        model.addAttribute(
//                "databases",
//                service.getDatabases());
//        
//        model.addAttribute(
//                "ags",
//                service.getAGStatus());
//        
//        model.addAttribute(
//                "clusters",
//                service.getClusterStatus());
//        
//        model.addAttribute(
//                "disks",
//                service.getDiskStatus());
//        
//        model.addAttribute(
//                "resources",
//                service.getServerResources());
//        
//        model.addAttribute(
//                "jobs",
//                service.getCriticalJobs());
//
//        model.addAttribute(
//                "servers",
//                service.getServers());
//        
//        model.addAttribute(
//                "connections",
//                service.testConnections());
//        
//        return "dashboard";
//    }
//}



package com.healthcheck;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RequestParam;
import jakarta.servlet.http.HttpServletRequest;


import java.sql.Connection;
@Controller
public class HealthController {

    @Autowired
    private SqlHealthService service;

    // Landing Page

    @GetMapping("/")
    public String home() {

        return "redirect:/sites";
    }

    
    
    
    // Health-Dashboard

    @GetMapping("/site/{site}/dashboard/{id}")
    public String healthDashboard(
            @PathVariable String site,
            @PathVariable int id,
            Model model) {

        try {

//            ServerInventory server =
//                    service.getServerByName(id);
            
            
            ServerInventory server =
                    service.getServerById(id);

            System.out.println(
                    "Dashboard Request -> "
                    + "ID=" + id
                    + ", Server=" + server.getServerName()
                    + ", Instance=" + server.getInstanceName()
                    + ", Port=" + server.getPortNumber());

            Connection con =
                    service.getTargetConnection(server);



            model.addAttribute(
                    "site",
                    site);

            model.addAttribute(
                    "server",
                    server);

            model.addAttribute(
                    "report",
                    service.getHealthReport(con));

            model.addAttribute(
                    "databases",
                    service.getDatabases(con));

            model.addAttribute(
                    "resources",
                    service.getServerResources(con));

            model.addAttribute(
                    "jobs",
                    service.getCriticalJobs(con));

            model.addAttribute(
                    "disks",
                    service.getDiskStatus(con));

            model.addAttribute(
                    "ags",
                    service.getAGStatus(con));

            model.addAttribute(
                    "clusters",
                    service.getClusterStatus(con));

            model.addAttribute(
                    "lastRefreshTime",
                    LocalDateTime.now()
                    .format(
                        DateTimeFormatter.ofPattern(
                            "dd-MMM-yyyy HH:mm:ss")));

            model.addAttribute(
                    "blockingSummary",
                    service.getBlockingSummary(con));

            return "dashboard-v2";

        }
        catch (Exception e) {

            e.printStackTrace();

            model.addAttribute(
                    "errorMessage",
                    e.getMessage());

            return "error";
        }
    }
    
    
//    Error Log Page
    
    
    @GetMapping(
            "/site/{site}/dashboard/{name}/errorlogs")
    public String errorLogs(
            @PathVariable String site,
            @PathVariable String name,
            Model model) {

        try {

            model.addAttribute(
                    "site",
                    site);

            model.addAttribute(
                    "serverName",
                    name);

            ServerInventory server =
                    service.getServerByName(name);

            Connection con =
                    service.getTargetConnection(server);

            model.addAttribute(
                    "errorLogs",
                    service.getCriticalErrorLogs(con));
        }
        catch(Exception e) {

            e.printStackTrace();

            model.addAttribute(
                    "errorLogs",
                    new ArrayList<ErrorLogInfo>());

            model.addAttribute(
                    "errorMessage",
                    e.getMessage());
        }

        return "error-logs";
    }
    
   // Site wise addition 
    
    @GetMapping("/sites")
    public String sites(Model model) {

        model.addAttribute(
                "sites",
                service.getSites());

        model.addAttribute(
                "lastRefreshTime",
                service.getLastRefreshTime());

        model.addAttribute(
                "refreshRunning",
                service.isRefreshRunning());

        return "sites";
    }
    
    @GetMapping("/site/{site}")
    public String siteServers(
            @PathVariable String site,
            Model model) {

        model.addAttribute(
                "site",
                site);

        model.addAttribute(
                "connections",
                service.getServerHealthBySite(site));

        return "server-list";
    }
    
    // Manual refresh button for refreshing in the background , as the refresh complete it automatically update the page.
    
    @GetMapping("/refresh-health")
    public String refreshHealth(
            RedirectAttributes redirectAttributes) {

        if(service.isRefreshRunning()) {

            redirectAttributes.addFlashAttribute(
                    "warningMessage",
                    "Health refresh is already running.");

            return "redirect:/sites";
        }

        new Thread(() -> {

            try {

                service.refreshServerHealth();

            }
            catch(Exception e) {

                e.printStackTrace();
            }

        }).start();

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Health refresh started successfully.");

        return "redirect:/sites";
    }
    
    
//    @GetMapping("/testAuth")
//    @ResponseBody
//    public String testAuth() {
//
//        AuthorizationService authService =
//                new AuthorizationService();
//
//        AuthorizedUser user =
//                authService.getAuthorizedUser("RCHOUD13");
//
//        if (user == null) {
//            return "User Not Found";
//        }
//
//        return user.toString();
//    }
    
//    
//    @GetMapping("/access-denied")
//    public String accessDenied(
//            HttpServletRequest request,
//            Model model) {
//
//        model.addAttribute(
//                "username",
//                request.getSession()
//                        .getAttribute("deniedUser"));
//
//        return "access-denied";
//    }

    
    
}