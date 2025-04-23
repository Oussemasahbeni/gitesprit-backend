package com.esprit.gitesprit.logs.domain.enums;

/** Represents the functional module or primary entity type related to the log entry. */
public enum ModuleType {
  // --- Core Entities ---
  USER,
  CLIENT,
  SUPPLIER,
  PRODUCT,
  CATEGORY, // Includes SubCategory actions
  SHOP,
  ROLE,
  PERMISSION,

  // --- Operational Modules ---
  AUTH, // Authentication/Authorization related actions
  POS, // Point of Sale / Caisse operations
  SALE, // Represents the Sale/Bon de Commande entity itself
  PAYMENT,
  INVENTORY, // General Stock/Inventory actions
  STOCK_ENTRY, // Specific stock additions
  PRODUCT_STOCK, // Stock level tracking/alerts
  INVENTORY_ITEM, // Specific serialized item tracking
  REPAIR_TICKET, // Repair ticketing system
  REPAIR_TASK, // Specific tasks within a repair
  INVOICE,
  CLIENT_CREDIT,
  SUPPLIER_CREDIT,
  RETURN, // Client returns process
  DELIVERY, // Delivery ticketing system
  EXPENSE,
  TIMECLOCK, // Pointage / Time Clock module
  NOTIFICATION,

  // --- Specific Features ---
  ZAKAT,
  TRAINING_COURSE,
  TRAINING_SESSION,
  REPORTING, // Generating reports/stats
  DASHBOARD, // Actions related to the dashboard view/widgets

  // --- System Level ---
  SYSTEM, // General system operations, configuration
  LOG // Actions related to viewing/managing logs themselves (optional)
}
