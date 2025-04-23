package com.esprit.gitesprit.logs.domain.enums;

/** Represents the type of action performed for logging purposes. */
public enum ActionType {
  // --- General CRUD ---
  CREATE, // Creating a new entity (User, Client, Product, Ticket, etc.)
  UPDATE, // Modifying an existing entity
  DELETE, // Deleting an entity (role-dependent)
  VIEW, // Viewing details of a specific entity
  LIST, // Listing multiple entities
  SEARCH, // Performing a search/filter operation
  EXPORT, // Exporting data (PDF, CSV)

  // --- Authentication & Session ---
  LOGIN, // User successfully logged in (username/password)
  LOGOUT, // User logged out
  LOGIN_FAILURE, // Failed login attempt
  BADGE_SCAN_AUTH, // User authenticated via badge scan
  PASSWORD_RESET_REQUEST, // User requested password reset
  PASSWORD_RESET_COMPLETE, // User completed password reset

  // --- Point of Sale (Caisse) ---
  ADD_TO_BASKET, // Adding an item to the sales basket
  REMOVE_FROM_BASKET, // Removing an item from the sales basket
  APPLY_DISCOUNT, // Applying a discount (item or global)
  CLEAR_BASKET, // Clearing the sales basket
  PROCESS_SALE, // Completing a sale transaction (generating Bon de Commande)
  OPEN_CASH_DRAWER, // Command sent to open physical cash drawer

  // --- Payments ---
  RECORD_PAYMENT, // Recording any payment (cash, cheque, card, credit)
  VALIDATE_PAYMENT, // Validating specific payment types (e.g., cheque details, card ref)
  SETTLE_CREDIT, // Specifically settling a client or supplier credit

  // --- Repairs (Tickets) ---
  ASSIGN_REPAIR, // Assigning a repair ticket to a technician
  UPDATE_REPAIR_STATUS, // Changing the status of a repair ticket
  REACTIVATE_REPAIR, // Reactivating a previously closed/failed repair ticket
  COMPLETE_REPAIR, // Marking a repair as technically complete
  CLOSE_REPAIR, // Closing a repair ticket (usually after payment/pickup)
  ADD_REPAIR_NOTE, // Adding notes/updates to a repair ticket
  ABANDONED_REPAIR_UPDATE, // Changing status due to >3 months abandonment

  // --- Stock ---
  ADD_STOCK, // Adding new stock items
  ADJUST_STOCK, // Manual stock adjustment (less common, maybe for corrections)
  VALIDATE_STOCK_ADD, // Admin validates a stock addition without refs
  SET_MIN_STOCK, // Setting minimum stock level for a product

  // --- Invoicing ---
  GENERATE_INVOICE, // Generating an invoice from Bons de Commande
  CANCEL_INVOICE, // Cancelling a generated invoice (Admin action)

  // --- Returns ---
  REQUEST_RETURN, // Client return request initiated
  VALIDATE_RETURN, // Admin validates a return request
  REJECT_RETURN, // Admin rejects a return request

  // --- Deliveries ---
  ASSIGN_DELIVERY, // Assigning a delivery ticket to a driver
  UPDATE_DELIVERY_STATUS, // Driver updates delivery status

  // --- User/Role Management ---
  ASSIGN_ROLE, // Assigning a role to a user
  UPDATE_PERMISSIONS, // Modifying permissions for a role
  ACTIVATE_USER, // Activating a user account
  DEACTIVATE_USER, // Deactivating a user account

  // --- Time Clock (Pointage) ---
  CLOCK_IN, // Employee clocks in
  CLOCK_OUT, // Employee clocks out
  ADD_TRUSTED_DEVICE, // Admin adds a device for clock-in

  // --- Expenses ---
  RECORD_EXPENSE, // Recording a fixed or variable expense
  RECORD_SALARY_ADVANCE, // Recording an advance payment to employee
  CONFIRM_ADVANCE_RECEIPT, // Employee confirms receipt of advance (badge scan)

  // --- Notifications ---
  SEND_NOTIFICATION, // System or User sends a notification
  MARK_NOTIFICATION_READ, // User marks a notification as read

  // --- Zakat ---
  CALCULATE_ZAKAT, // System or Admin calculates Zakat
  MARK_ZAKAT_PAID, // Admin marks Zakat as paid

  // --- Training ---
  REGISTER_LEARNER, // Registering a learner for a training session
  RECORD_TRAINING_RESULT, // Recording the result of a training session

  CHANGE_PASSWORD, // --- System/Admin ---
  TOGGLE_ACCOUNT_ENABLED,
  TOGGLE_ACCOUNT_LOCKED,
  RESET_PASSWORD,
  ACCOUNT_ACTIVATION,
  CATEGORY_CREATE,
  ADD_SUB_CATEGORY,
  REMOVE_SUB_CATEGORY,
  CATEGORY_DELETE,
  CATEGORY_UPDATE,
  ADD_USER,
  DELETE_USER,
  SYSTEM_CONFIG_UPDATE // General system configuration change
}
