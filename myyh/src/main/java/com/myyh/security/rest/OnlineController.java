package com.myyh.security.rest;

import com.myyh.security.service.OnlineUserService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @author Zheng Jie
 */
@RestController
@RequestMapping("/auth/online")
public class OnlineController {

    private final OnlineUserService onlineUserService;

    public OnlineController(OnlineUserService onlineUserService) {
        this.onlineUserService = onlineUserService;
    }

    @GetMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> getAll(String filter, Pageable pageable){
        return new ResponseEntity<>(onlineUserService.getAll(filter, pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/download")
    @PreAuthorize("@el.check()")
    public void download(HttpServletResponse response, String filter) throws IOException {
        onlineUserService.download(onlineUserService.getAll(filter), response);
    }

    @DeleteMapping
    @PreAuthorize("@el.check()")
    public ResponseEntity<Object> delete(@RequestBody Set<String> keys) throws Exception {
        for (String key : keys) {
            onlineUserService.kickOut(key);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
