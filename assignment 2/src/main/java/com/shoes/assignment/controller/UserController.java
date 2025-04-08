package com.shoes.assignment.controller;

import com.shoes.assignment.model.Order;
import com.shoes.assignment.model.Shoe;
import com.shoes.assignment.model.User;
import com.shoes.assignment.service.OrderService;
import com.shoes.assignment.service.RoleService;
import com.shoes.assignment.service.ShoeService;
import com.shoes.assignment.service.UserService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private ShoeService shoeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/user/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/user/signup")
    public String signupUser(@ModelAttribute("user") User user, Model model) {
        user.setRole(roleService.getRoleById(4L).orElseThrow(() -> new RuntimeException("Default role not found")));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.saveUser(user);
        return "redirect:/home";
    }

    @GetMapping("/login")
    public String showLoginPage(Model model, @RequestParam(required = false) String error) {
        model.addAttribute("user", new User());
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        return "Login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") User user, Model model, HttpSession session) {
        Optional<User> optionalUser = userService.findByEmail(user.getEmail());
        if (optionalUser.isPresent()) {
            User existingUser = optionalUser.get();
            Long roleId = existingUser.getRole().getRoleId();
            model.addAttribute("user", existingUser);
            session.setAttribute("currentUser", existingUser); // Set currentUser in session
            if (roleId == 1) {
                return "redirect:/manager/report"; // Manager
            } else if (roleId == 2) {
                model.addAttribute("shoe", new Shoe());
                return "ClerkAddItem"; // Clerk
            } else if (roleId == 3) {
                return "redirect:/accountant/report"; // Accountant
            } else if (roleId == 4) {
                if (passwordEncoder.matches(user.getPassword(), existingUser.getPassword())) {
                    return "redirect:/home"; // User
                } else {
                    model.addAttribute("error", "Invalid username or password");
                    return "Login";
                }
            }
        }
        model.addAttribute("error", "Invalid username or password");
        return "Login";
    }

    @GetMapping("/home") //No Image
    public String showUserHome(Model model) {
        List<Shoe> shoes = shoeService.getShoesWithImages();
        model.addAttribute("shoes", shoes);
        return "home";
    }

    @PostMapping("/cart/add/{shoeId}") //Still can't use remove button
    public String addToCart(@PathVariable Long shoeId, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        Shoe shoe = shoeService.getShoeById(shoeId).orElseThrow(() -> new RuntimeException("Shoe not found"));
        List<Order> cart = (List<Order>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }

        boolean itemExists = false;
        for (Order order : cart) {
            if (order.getShoe().getShoeId().equals(shoeId)) {
                order.setQuantity(order.getQuantity() + 1);
                order.setTotalPrice(order.getTotalPrice() + shoe.getPrice());
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            Order order = new Order();
            order.setUser(currentUser);
            order.setShoe(shoe);
            order.setQuantity(1);
            order.setTotalPrice(shoe.getPrice());
            cart.add(order);
        }

        session.setAttribute("cart", cart);
        return "redirect:/home";
    }

    @GetMapping("/cart")
    public String showCart(Model model, HttpSession session) {
        List<Order> cart = (List<Order>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        double totalAmount = cart.stream().mapToDouble(Order::getTotalPrice).sum();
        model.addAttribute("cartItems", cart);
        model.addAttribute("totalAmount", totalAmount);
        return "cart";
    }

    @PostMapping("/checkout")
    public String showCheckoutPage(@RequestParam List<Long> orderIds, Model model, HttpSession session) {
        List<Order> cart = (List<Order>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
        }
        List<Order> checkoutItems = cart.stream()
                                         .filter(order -> orderIds.contains(order.getOrderId()))
                                         .collect(Collectors.toList());

        double totalAmount = checkoutItems.stream().mapToDouble(Order::getTotalPrice).sum();
        model.addAttribute("cartItems", checkoutItems);
        model.addAttribute("totalAmount", totalAmount);
        return "checkout";
    }

    @PostMapping("/placeOrder") //It work. 
    public String placeOrder(@RequestParam String name, @RequestParam String address, 
                             @RequestParam String email, @RequestParam String contact1,
                             @RequestParam String contact2, @RequestParam String payment, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        List<Order> cart = (List<Order>) session.getAttribute("cart");
        if (cart == null) {
            return "redirect:/home";
        }

        LocalDate today = LocalDate.now();

        for (Order order : cart) {
            order.setDeliveryAddress(address);
            order.setContact1(contact1);
            order.setContact2(contact2);
            order.setOrderDate(today); 
            orderService.saveOrder(order);
        }

        session.removeAttribute("cart");
        return "redirect:/home";
    }

    
    @GetMapping("/admin/clerk/ClerkAddItem/{id}") //It's work. 
    public String showAddItemForm(@PathVariable Long id, Model model, HttpSession session) {
        Optional<User> optionalUser = userService.getUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            model.addAttribute("user", user);
            model.addAttribute("shoe", new Shoe());
            return "ClerkAddItem";
        }
        return "redirect:/login";
    }

    @PostMapping("/admin/clerk/ClerkAddItem/{id}") //It's work. 
    public String addItem(@PathVariable Long id, @ModelAttribute("shoe") Shoe shoe, @RequestParam("fimage") MultipartFile fimage, HttpSession session, Model model) {
        Optional<User> optionalUser = userService.getUserById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            try {
                if (!fimage.isEmpty()) {
                    byte[] bytes = fimage.getBytes();
                    String image = new String(bytes);
                    shoe.setImage(image);
                }
                shoeService.saveShoe(shoe);
                model.addAttribute("successMessage", "Shoe added successfully!");
            } catch (IOException e) {
                model.addAttribute("errorMessage", "Error saving shoe image");
                e.printStackTrace();
            }
        }
        return "redirect:/admin/clerk/ClerkAddItem/{id}";
    }
    
//    @PostMapping("/admin/clerk/ClerkAddItem/{id}")
//    public String addItem(Model model,@PathVariable Long id,Shoe shoe){
//      Optional<User> optionalUser = userService.getUserById(id);
//      if(optionalUser.isPresent()) {
//    	  User user = optionalUser.get();
//    	  shoeService.saveShoe(shoe);
//    	  System.out.println(shoe.getImage());
//      }
//      return "redirect:/admin/clerk/ClerkAddItem/{id}";
//    }
    

    @GetMapping("/accountant/report") //It work! 
    public String showMonthlyReport(Model model) {
        List<Order> orders = orderService.getAllOrders();
        double totalIncome = orders.stream().mapToDouble(Order::getTotalPrice).sum();

        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("orders", orders);

        return "AccReal";
    }
    
    @GetMapping("/accountant/income-chart")
    public String showIncomeChart(Model model) {
        List<Order> orders = orderService.getAllOrders();

        // Aggregate total income per shoe
        Map<Long, Double> shoeIncomeMap = orders.stream()
            .collect(Collectors.groupingBy(order -> order.getShoe().getShoeId(), 
                    Collectors.summingDouble(Order::getTotalPrice)));

        List<String> shoeIds = shoeIncomeMap.keySet().stream()
            .map(String::valueOf)
            .collect(Collectors.toList());
        List<Double> orderTotals = new ArrayList<>(shoeIncomeMap.values());

        double totalIncome = orderTotals.stream().mapToDouble(Double::doubleValue).sum();

        model.addAttribute("shoeIds", shoeIds);
        model.addAttribute("orderTotals", orderTotals);
        model.addAttribute("totalIncome", totalIncome);

        return "incomeChart";
    }
    
    @GetMapping("/manager/report")
    public String showDailyIncomeReport(Model model) {
    	List<Order> orders = orderService.getAllOrders();
        double totalIncome = orders.stream().mapToDouble(Order::getTotalPrice).sum();

        model.addAttribute("totalIncome", totalIncome);
        model.addAttribute("orders", orders);

        return "Manager";
    }
    
    
    


        
}
