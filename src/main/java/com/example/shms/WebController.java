package com.example.shms;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Controller
public class WebController {
    private final StudentRepository students;
    private final WardenRepository wardens;
    private final AccountantRepository accountants;
    private final FeePaymentRepository feePayments;
    private final RoomRepository rooms;
    private final ComplaintRepository complaints;
    private final OutPassRepository outPasses;
    private final VisitorRepository visitors;
    private final AttendanceRepository attendance;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public WebController(StudentRepository students, WardenRepository wardens, AccountantRepository accountants, FeePaymentRepository feePayments, RoomRepository rooms, ComplaintRepository complaints,
                         OutPassRepository outPasses, VisitorRepository visitors, AttendanceRepository attendance) {
        this.students=students; this.wardens=wardens; this.accountants=accountants; this.feePayments=feePayments; this.rooms=rooms; this.complaints=complaints; this.outPasses=outPasses;
        this.visitors=visitors; this.attendance=attendance;
    }

    @GetMapping("/") public String root(){return "redirect:/login";}
    @GetMapping("/login") public String login(){return "login";}
    @PostMapping("/login") public String doLogin(@RequestParam String username,@RequestParam String password,
        @RequestParam String role, jakarta.servlet.http.HttpSession session, RedirectAttributes ra) {
        if (!List.of("Student", "Warden", "Accountant").contains(role)) {
            ra.addFlashAttribute("error", "Please select a valid account type.");
            return "redirect:/login";
        }
        boolean ok = false;
        if(role.equals("Warden")) {
            Optional<Warden> w = wardens.findByUsername(username);
            ok = (w.isPresent() && encoder.matches(password, w.get().getPasswordHash()))
                || (username.equals("warden") && password.equals("warden123"));
        }
        else if(role.equals("Accountant")) {
            Optional<Accountant> a=accountants.findByUsername(username);
            ok=a.isPresent()&&encoder.matches(password,a.get().getPasswordHash());
        }
        else {
            Optional<Student> s=students.findByUsername(username);
            ok=s.isPresent()&&encoder.matches(password,s.get().getPasswordHash());
        }
        if(!ok){ra.addFlashAttribute("error","Invalid login details. Please try again.");return "redirect:/login";}
        session.setAttribute("role",role); session.setAttribute("username",username);
        return "redirect:/dashboard";
    }
    @GetMapping("/register") public String register(){return "register";}

    @PostMapping("/register/student") public String registerStudent(@RequestParam String studentId,@RequestParam String fullName,
        @RequestParam String username,@RequestParam String password,@RequestParam String email,@RequestParam String phone,
        @RequestParam String course,@RequestParam String yearOfStudy,RedirectAttributes ra) {
        if(students.findByStudentId(studentId.trim()).isPresent() || usernameTaken(username.trim())){
            ra.addFlashAttribute("error","Student ID or username already exists."); return "redirect:/register";
        }
        if(password.length()<8){ra.addFlashAttribute("error","Password must be at least 8 characters.");return "redirect:/register";}
        Student s=new Student(); s.setStudentId(studentId.trim());s.setFullName(fullName.trim());s.setUsername(username.trim());
        s.setPasswordHash(encoder.encode(password));s.setEmail(email.trim());s.setPhone(phone.trim());s.setCourse(course.trim());
        s.setYearOfStudy(yearOfStudy);s.setApplicationStatus("Not Applied");s.setFeeStatus("Pending");students.save(s);
        ra.addFlashAttribute("success","Student account created. You can now sign in as Student."); return "redirect:/login";
    }

    @PostMapping("/register/warden") public String registerWarden(@RequestParam String wardenId,@RequestParam String fullName,
        @RequestParam String username,@RequestParam String password,@RequestParam String email,@RequestParam String phone,
        @RequestParam String assignedHostel,RedirectAttributes ra) {
        if(wardens.findByWardenId(wardenId.trim()).isPresent() || usernameTaken(username.trim())){
            ra.addFlashAttribute("error","Warden ID or username already exists."); return "redirect:/register";
        }
        if(password.length()<8){ra.addFlashAttribute("error","Password must be at least 8 characters.");return "redirect:/register";}
        Warden w = new Warden(); w.setWardenId(wardenId.trim()); w.setFullName(fullName.trim());
        w.setUsername(username.trim()); w.setPasswordHash(encoder.encode(password)); w.setEmail(email.trim());
        w.setPhone(phone.trim()); w.setAssignedHostel(assignedHostel.trim()); wardens.save(w);
        ra.addFlashAttribute("success","Warden account created. You can now sign in as Warden."); return "redirect:/login";
    }

    @PostMapping("/register/accountant") public String registerAccountant(@RequestParam String accountantId,@RequestParam String fullName, @RequestParam String username,@RequestParam String password,@RequestParam String email,@RequestParam String phone,RedirectAttributes ra) {
        if(accountants.findByAccountantId(accountantId.trim()).isPresent() || usernameTaken(username.trim())) { ra.addFlashAttribute("error","Accountant ID or username already exists."); return "redirect:/register"; }
        if(password.length()<8) { ra.addFlashAttribute("error","Password must be at least 8 characters."); return "redirect:/register"; }
        Accountant a=new Accountant(); a.setAccountantId(accountantId.trim()); a.setFullName(fullName.trim()); a.setUsername(username.trim()); a.setPasswordHash(encoder.encode(password)); a.setEmail(email.trim()); a.setPhone(phone.trim()); accountants.save(a);
        ra.addFlashAttribute("success","Accountant account created. Sign in as Accountant."); return "redirect:/login";
    }

    private boolean usernameTaken(String username) {
        return students.findByUsername(username).isPresent() || wardens.findByUsername(username).isPresent() || accountants.findByUsername(username).isPresent()
            || username.equalsIgnoreCase("admin");
    }
    private boolean guard(jakarta.servlet.http.HttpSession session, Model model) {
        if(session.getAttribute("role")==null)return false;
        model.addAttribute("role",session.getAttribute("role")); model.addAttribute("username",session.getAttribute("username"));return true;
    }
    private String deny(jakarta.servlet.http.HttpSession session){return session.getAttribute("role")==null?"redirect:/login":"redirect:/dashboard";}
    @GetMapping("/logout") public String logout(jakarta.servlet.http.HttpSession s){s.invalidate();return "redirect:/login";}

    @GetMapping("/dashboard") public String dashboard(Model m,jakarta.servlet.http.HttpSession s){
        if(!guard(s,m))return "redirect:/login";
        m.addAttribute("studentCount",students.count());
        m.addAttribute("feePaymentCount",feePayments.count());
        m.addAttribute("feeCollection",feePayments.findAll().stream().mapToDouble(fp->fp.getAmount()==null?0:fp.getAmount()).sum());m.addAttribute("roomCount",rooms.count());
        m.addAttribute("occupiedBeds",rooms.findAll().stream().mapToInt(r->r.getOccupied()==null?0:r.getOccupied()).sum());
        m.addAttribute("bedCount",rooms.findAll().stream().mapToInt(r->r.getCapacity()==null?0:r.getCapacity()).sum());
        m.addAttribute("openComplaints",complaints.findAll().stream().filter(c->!"Resolved".equals(c.getStatus())).count());
        m.addAttribute("pendingPasses",outPasses.findAll().stream().filter(p->"Pending".equals(p.getStatus())).count());
        m.addAttribute("recentComplaints",complaints.findAll().stream().sorted(Comparator.comparing(Complaint::getCreatedAt).reversed()).limit(5).toList());
        return "dashboard";
    }
    @GetMapping("/students") public String students(Model m,jakarta.servlet.http.HttpSession s){if(!guard(s,m))return "redirect:/login";if(!"Warden".equals(s.getAttribute("role")))return deny(s);m.addAttribute("students",students.findAll());return "students";}
    @PostMapping("/students/{id}/fee") public String fee(@PathVariable Long id,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){if(!"Accountant".equals(s.getAttribute("role")))return deny(s);students.findById(id).ifPresent(st->{st.setFeeStatus("Paid");students.save(st);});ra.addFlashAttribute("success","Fee status updated (demo action).");return "redirect:/students";}
    @GetMapping("/fees") public String fees(Model m,jakarta.servlet.http.HttpSession s){if(!guard(s,m))return "redirect:/login";if(!"Accountant".equals(s.getAttribute("role")))return deny(s);m.addAttribute("students",students.findAll());m.addAttribute("payments",feePayments.findAll().stream().sorted(Comparator.comparing(FeePayment::getPaidAt,Comparator.nullsLast(Comparator.reverseOrder()))).toList());m.addAttribute("pendingCount",students.findAll().stream().filter(st->!"Paid".equals(st.getFeeStatus())).count());m.addAttribute("collection",feePayments.findAll().stream().mapToDouble(fp->fp.getAmount()==null?0:fp.getAmount()).sum());return "fees";}
    @PostMapping("/fees/record") public String recordFee(@RequestParam String studentId,@RequestParam String feeType,@RequestParam Double amount,@RequestParam String paymentMode,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){if(!"Accountant".equals(s.getAttribute("role")))return deny(s);if(amount==null||amount<=0){ra.addFlashAttribute("error","Amount must be greater than zero.");return "redirect:/fees";}Optional<Student> os=students.findByStudentId(studentId.trim());if(os.isEmpty()){ra.addFlashAttribute("error","Student ID not found.");return "redirect:/fees";}Student st=os.get();FeePayment fp=new FeePayment();fp.setStudentId(st.getStudentId());fp.setStudentName(st.getFullName());fp.setFeeType(feeType);fp.setAmount(amount);fp.setPaymentMode(paymentMode);fp.setReceiptNumber("SHMS-"+System.currentTimeMillis());fp.setPaidAt(LocalDateTime.now());feePayments.save(fp);st.setFeeStatus("Paid");students.save(st);ra.addFlashAttribute("success","Payment recorded. Receipt: "+fp.getReceiptNumber());return "redirect:/fees";}
    @GetMapping("/rooms") public String rooms(Model m,jakarta.servlet.http.HttpSession s){if(!guard(s,m))return "redirect:/login";if(!isStaff(s))return deny(s);m.addAttribute("rooms",rooms.findAll());return "rooms";}
    @PostMapping("/rooms/allocate") public String allocate(@RequestParam String studentId,@RequestParam Integer roomNumber,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){
        if(!isStaff(s))return deny(s);
        Optional<Student> os=students.findByStudentId(studentId);Optional<Room> or=rooms.findByRoomNumber(roomNumber);
        if(os.isEmpty()||or.isEmpty()){ra.addFlashAttribute("error","Student or room not found.");return "redirect:/rooms";}
        Student st=os.get();Room r=or.get();
        if(st.getRoomNumber()!=null){ra.addFlashAttribute("error","Student already has a room. Transfer them first.");return "redirect:/rooms";}
        if(r.getOccupied()>=r.getCapacity()){ra.addFlashAttribute("error","Selected room is full.");return "redirect:/rooms";}
        st.setRoomNumber(roomNumber);st.setApplicationStatus("Approved");r.setOccupied(r.getOccupied()+1);r.setStatus(r.getOccupied()>=r.getCapacity()?"Full":"Available");
        students.save(st);rooms.save(r);ra.addFlashAttribute("success","Room allocated successfully.");return "redirect:/rooms";
    }
    @PostMapping("/rooms/transfer") public String transfer(@RequestParam String studentId,@RequestParam Integer roomNumber,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){
        if(!isStaff(s))return deny(s);Optional<Student> os=students.findByStudentId(studentId);Optional<Room> nr=rooms.findByRoomNumber(roomNumber);
        if(os.isEmpty()||nr.isEmpty()){ra.addFlashAttribute("error","Student or destination room not found.");return "redirect:/rooms";}
        Student st=os.get();Room dest=nr.get();if(st.getRoomNumber()==null){ra.addFlashAttribute("error","Student has no current room; use allocation.");return "redirect:/rooms";}
        if(dest.getRoomNumber().equals(st.getRoomNumber())){ra.addFlashAttribute("error","Choose a different room.");return "redirect:/rooms";}
        if(dest.getOccupied()>=dest.getCapacity()){ra.addFlashAttribute("error","Destination room is full.");return "redirect:/rooms";}
        rooms.findByRoomNumber(st.getRoomNumber()).ifPresent(old->{old.setOccupied(Math.max(0,old.getOccupied()-1));old.setStatus("Available");rooms.save(old);});
        st.setRoomNumber(roomNumber);dest.setOccupied(dest.getOccupied()+1);dest.setStatus(dest.getOccupied()>=dest.getCapacity()?"Full":"Available");students.save(st);rooms.save(dest);
        ra.addFlashAttribute("success","Room transfer completed.");return "redirect:/rooms";
    }
    @GetMapping("/complaints") public String complaints(Model m,jakarta.servlet.http.HttpSession s){if(!guard(s,m))return "redirect:/login";List<Complaint> complaintList=complaints.findAll(); if("Student".equals(s.getAttribute("role"))){String sid=students.findByUsername((String)s.getAttribute("username")).map(Student::getStudentId).orElse(""); complaintList=complaintList.stream().filter(c->sid.equals(c.getStudentId())).toList();} else if(!"Warden".equals(s.getAttribute("role"))) return deny(s); m.addAttribute("complaints",complaintList.stream().sorted(Comparator.comparing(Complaint::getCreatedAt).reversed()).toList());return "complaints";}
    @PostMapping("/complaints") public String addComplaint(@RequestParam String category,@RequestParam String description,
        @RequestParam(required=false) String studentId,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){
        if(s.getAttribute("role")==null)return "redirect:/login";
        String sid=studentId;String name=String.valueOf(s.getAttribute("username"));
        if("Student".equals(s.getAttribute("role"))){Optional<Student> st=students.findByUsername(name);if(st.isEmpty())return "redirect:/login";sid=st.get().getStudentId();name=st.get().getFullName();}
        if(sid==null||sid.isBlank()||description.isBlank()){ra.addFlashAttribute("error","Please provide student ID and complaint details.");return "redirect:/complaints";}
        Complaint c=new Complaint();c.setStudentId(sid);c.setStudentName(name);c.setCategory(category);c.setDescription(description);c.setStatus("Open");c.setCreatedAt(LocalDateTime.now());complaints.save(c);
        ra.addFlashAttribute("success","Complaint submitted.");return "redirect:/complaints";
    }
    @PostMapping("/complaints/{id}/status") public String complaintStatus(@PathVariable Long id,@RequestParam String status,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){
        if(!isStaff(s))return deny(s);if(!List.of("Open","In Progress","Resolved").contains(status)){ra.addFlashAttribute("error","Invalid status.");return "redirect:/complaints";}
        complaints.findById(id).ifPresent(c->{c.setStatus(status);complaints.save(c);});ra.addFlashAttribute("success","Complaint status updated.");return "redirect:/complaints";
    }
    @GetMapping("/outpasses") public String outpasses(Model m,jakarta.servlet.http.HttpSession s){if(!guard(s,m))return "redirect:/login";List<OutPass> passList=outPasses.findAll(); if("Student".equals(s.getAttribute("role"))){String sid=students.findByUsername((String)s.getAttribute("username")).map(Student::getStudentId).orElse(""); passList=passList.stream().filter(p->sid.equals(p.getStudentId())).toList();} else if(!"Warden".equals(s.getAttribute("role"))) return deny(s); m.addAttribute("outpasses",passList.stream().sorted(Comparator.comparing(OutPass::getDepartureAt,Comparator.nullsLast(Comparator.naturalOrder())).reversed()).toList());return "outpasses";}
    @PostMapping("/outpasses") public String addPass(@RequestParam String reason,@RequestParam String destination,@RequestParam String departureAt,@RequestParam String returnAt,
        @RequestParam(required=false) String studentId,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){
        if(s.getAttribute("role")==null)return "redirect:/login";
        String sid=studentId;String name=String.valueOf(s.getAttribute("username"));
        if("Student".equals(s.getAttribute("role"))){Optional<Student> st=students.findByUsername(name);if(st.isEmpty())return "redirect:/login";sid=st.get().getStudentId();name=st.get().getFullName();}
        try{LocalDateTime dep=LocalDateTime.parse(departureAt),ret=LocalDateTime.parse(returnAt);if(!ret.isAfter(dep))throw new IllegalArgumentException();
            OutPass op=new OutPass();op.setStudentId(sid);op.setStudentName(name);op.setReason(reason);op.setDestination(destination);op.setDepartureAt(dep);op.setReturnAt(ret);op.setStatus("Pending");outPasses.save(op);
            ra.addFlashAttribute("success","Out-pass request submitted for warden approval.");
        }catch(Exception e){ra.addFlashAttribute("error","Check the date/time values. Return time must be after departure.");}
        return "redirect:/outpasses";
    }
    @PostMapping("/outpasses/{id}/decision") public String passDecision(@PathVariable Long id,@RequestParam String status,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){
        if(!"Warden".equals(s.getAttribute("role")))return deny(s);
        if(!List.of("Approved","Rejected").contains(status))return "redirect:/outpasses";
        outPasses.findById(id).ifPresent(p->{p.setStatus(status);outPasses.save(p);});ra.addFlashAttribute("success","Out-pass decision saved.");return "redirect:/outpasses";
    }
    @GetMapping("/attendance") public String attendance(Model m,jakarta.servlet.http.HttpSession s){if(!guard(s,m))return "redirect:/login";if(!"Warden".equals(s.getAttribute("role"))&&!"Student".equals(s.getAttribute("role")))return deny(s);m.addAttribute("students", "Warden".equals(s.getAttribute("role")) ? students.findAll() : List.of());m.addAttribute("records",attendance.findAll().stream().sorted(Comparator.comparing(Attendance::getAttendanceDate).reversed()).limit(100).toList());return "attendance";}
    @PostMapping("/attendance") public String markAttendance(@RequestParam String studentId,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){
        if(!"Warden".equals(s.getAttribute("role")))return deny(s);
        LocalTime now=LocalTime.now();if(now.isBefore(LocalTime.of(21,0))||now.isAfter(LocalTime.of(21,30))){ra.addFlashAttribute("error","Attendance can only be recorded between 9:00 PM and 9:30 PM server time.");return "redirect:/attendance";}
        Optional<Student> os=students.findByStudentId(studentId);if(os.isEmpty()){ra.addFlashAttribute("error","Student ID not found.");return "redirect:/attendance";}
        if(attendance.findByStudentIdAndAttendanceDate(studentId,LocalDate.now()).isPresent()){ra.addFlashAttribute("error","Attendance already recorded today.");return "redirect:/attendance";}
        Attendance a=new Attendance();a.setStudentId(studentId);a.setStudentName(os.get().getFullName());a.setAttendanceDate(LocalDate.now());a.setStatus("Present");attendance.save(a);ra.addFlashAttribute("success","Attendance marked.");return "redirect:/attendance";
    }
    @GetMapping("/visitors") public String visitors(Model m,jakarta.servlet.http.HttpSession s){if(!guard(s,m))return "redirect:/login";if(!isStaff(s))return deny(s);m.addAttribute("visitors",visitors.findAll().stream().sorted(Comparator.comparing(Visitor::getEntryTime).reversed()).toList());return "visitors";}
    @PostMapping("/visitors") public String addVisitor(@RequestParam String visitorName,@RequestParam String studentId,@RequestParam String purpose,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){
        if(!isStaff(s))return deny(s);LocalTime now=LocalTime.now();if(now.isBefore(LocalTime.of(9,0))||now.isAfter(LocalTime.of(19,0))){ra.addFlashAttribute("error","Visitor entry is allowed between 9:00 AM and 7:00 PM server time.");return "redirect:/visitors";}
        if(students.findByStudentId(studentId).isEmpty()){ra.addFlashAttribute("error","Student ID not found.");return "redirect:/visitors";}
        Visitor v=new Visitor();v.setVisitorName(visitorName);v.setStudentId(studentId);v.setPurpose(purpose);v.setEntryTime(LocalDateTime.now());v.setStatus("Inside");visitors.save(v);ra.addFlashAttribute("success","Visitor checked in.");return "redirect:/visitors";
    }
    @PostMapping("/visitors/{id}/checkout") public String checkout(@PathVariable Long id,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){if(!isStaff(s))return deny(s);visitors.findById(id).ifPresent(v->{v.setStatus("Exited");v.setExitTime(LocalDateTime.now());visitors.save(v);});ra.addFlashAttribute("success","Visitor checked out.");return "redirect:/visitors";}
    @GetMapping("/reports") public String reports(Model m,jakarta.servlet.http.HttpSession s){if(!guard(s,m))return "redirect:/login";if(!"Warden".equals(s.getAttribute("role")))return deny(s);
        m.addAttribute("students",students.findAll());m.addAttribute("rooms",rooms.findAll());m.addAttribute("complaints",complaints.findAll());m.addAttribute("outpasses",outPasses.findAll());
        m.addAttribute("paidCount",students.findAll().stream().filter(st->"Paid".equals(st.getFeeStatus())).count());
        m.addAttribute("unresolved",complaints.findAll().stream().filter(c->!"Resolved".equals(c.getStatus())).count());return "reports";}
    @GetMapping("/profile") public String profile(Model m,jakarta.servlet.http.HttpSession s){if(!guard(s,m))return "redirect:/login";if(!"Student".equals(s.getAttribute("role")))return deny(s);m.addAttribute("student",students.findByUsername((String)s.getAttribute("username")).orElse(null));return "profile";}
    @PostMapping("/profile") public String updateProfile(@RequestParam String email,@RequestParam String phone,@RequestParam String course,jakarta.servlet.http.HttpSession s,RedirectAttributes ra){
        if(!"Student".equals(s.getAttribute("role")))return deny(s);students.findByUsername((String)s.getAttribute("username")).ifPresent(st->{st.setEmail(email);st.setPhone(phone);st.setCourse(course);students.save(st);});ra.addFlashAttribute("success","Profile updated.");return "redirect:/profile";}
    @PostMapping("/application") public String application(jakarta.servlet.http.HttpSession s,RedirectAttributes ra){
        if(!"Student".equals(s.getAttribute("role")))return deny(s);students.findByUsername((String)s.getAttribute("username")).ifPresent(st->{if(!"Pending".equals(st.getApplicationStatus())&&!"Approved".equals(st.getApplicationStatus()))st.setApplicationStatus("Pending");students.save(st);});
        ra.addFlashAttribute("success","Hostel application submitted. Warden/admin can allocate a room.");return "redirect:/dashboard";}
    private boolean isStaff(jakarta.servlet.http.HttpSession s){return "Warden".equals(s.getAttribute("role"));}
}
