package com.app.latecomer.controllers;

import com.app.latecomer.businesslogic.SettingsLogic;
import com.app.latecomer.model.MessageResponse;
import com.app.latecomer.model.Settings;
import com.app.latecomer.model.viewmodel.SettingsViewModel;
import com.app.latecomer.utils.Utility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SettingsController {
    SettingsLogic settingsLogic;
    Utility utility;

    public SettingsController(SettingsLogic settingsLogic, Utility utility) {
        this.settingsLogic = settingsLogic;
        this.utility = utility;
    }


    /**
     * This controller creates the meeting date time settings
     *
     * @param settings Request  body {@link SettingsViewModel} class
     *                 meetingDateTime format  dd-MM-yyyy hh:mm:ss
     * @return A {@link ResponseEntity<>} containing the {@link Settings} .
     */
    @Transactional
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public ResponseEntity<MessageResponse<Settings>> createSettings(@Valid @RequestBody SettingsViewModel settings) throws Exception {
        MessageResponse<Settings> messageResponse;
        try {
            Date dateTime = settings.getMeetingDateTime();
            Settings settingsObject = new Settings();
            settingsObject.setActivated(false);
            settingsObject.setMeetingDateTime(dateTime);
            settingsLogic.create(settingsObject);
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Settings created successfully");
            messageResponse.setData(settingsObject);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }


    /**
     * This controller activates meeting settings
     *
     * @param id Request  body {@link Integer} class
     * @return A {@link ResponseEntity<>} containing the {@link Settings} .
     */
    @Transactional
    @RequestMapping(value = "/settings/{id}", method = RequestMethod.PUT)
    public ResponseEntity<MessageResponse<Settings>> updateSettings(@PathVariable Integer id, @NotNull @NotEmpty @RequestParam Boolean status) throws Exception {
        MessageResponse<Settings> messageResponse;
        try {

            Settings settingsObject = settingsLogic.findOne(id);
            if (settingsObject == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Meeting settings not found!");

            }
            List<Settings> settingsList = settingsLogic.findAll();
            if (status) settingsLogic.deactActivateAllSettings(settingsList);
            settingsObject.setActivated(status);
            messageResponse = new MessageResponse<>();
            messageResponse.setMessage("Settings update successfully");
            messageResponse.setData(settingsObject);
            messageResponse.setStatus(HttpStatus.OK.value());
            return new ResponseEntity<>(messageResponse, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
}
