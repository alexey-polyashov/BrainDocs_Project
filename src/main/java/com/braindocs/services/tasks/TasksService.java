package com.braindocs.services.tasks;

import com.braindocs.common.Options;
import com.braindocs.services.users.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class TasksService {

    private final UserService userService;
    private final Options options;


}
