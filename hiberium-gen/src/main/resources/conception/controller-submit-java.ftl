
    @RequestMapping(value = "/${concept_apipath}/form", method = RequestMethod.GET)
    public String show${concept_name}Form(
            @RequestParam(value = "id", required = false) Integer ${concept_varname}Id,
            Model model
            ) throws Exception {
        model.addAttribute("${concept_varname}", new ${concept_name}());
        if(${concept_varname}Id != null) {
            try {
                ${concept_name} ${concept_varname} = service.findByPrimaryKey(${concept_varname}Id);
                model.addAttribute("${concept_varname}", ${concept_varname});
            } catch (Exception ignored) { }
        }
        return "submit-${concept_name}";
    }

    @RequestMapping(value = "/${concept_apipath}/submit", method = RequestMethod.POST)
    public String submit${concept_name}Form(
            @Valid @ModelAttribute("${concept_varname}") ${concept_name} ${concept_varname},
            BindingResult result
            ) throws Exception {
        if(result.hasErrors())
            return "submit-${concept_name}";
        Integer ${concept_varname}Id = service.handle${concept_name}InsertOrUpdate(${concept_varname}, "B");
        return "redirect:/${concept_apipath}/form?id="+${concept_varname}Id;
    }