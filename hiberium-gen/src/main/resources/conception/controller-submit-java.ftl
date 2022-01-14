
    @RequestMapping(value = "/${concept_apipath}/form", method = RequestMethod.GET)
    public String show${concept_name}Form(
             @RequestParam(value = "id", required = false) Integer ${concept_varname}Id,
             Model model
             ) throws Exception {
        model.addAttribute("${concept_varname}", new ${concept_name}());
        if(${concept_varname}Id != null) {
            ${concept_name} ${concept_varname} = service.findByPrimaryKey(${concept_varname}Id);
            model.addAttribute("${concept_varname}", ${concept_varname});
        }
        return "submit-${concept_name}";
    }

    @RequestMapping(value = "/${concept_apipath}/submit", method = RequestMethod.POST)
    public ResponseEntity<${concept_name}> submit${concept_name}Form(
             @Valid @ModelAttribute("${concept_varname}") ${concept_name} ${concept_varname},
             BindingResult result
             ) throws Exception {

        ${concept_varname}.handleFieldsForInsert();
        ${concept_name} ${concept_varname}Inserted = repository.save(${concept_varname});
        log.info("INSERT ${concept_name} where id={}", ${concept_varname}Inserted.primaryKey());

        return ResponseEntity.ok().body(${concept_varname}Inserted);
    }