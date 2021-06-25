
    @RequestMapping(value = "/${concept_apipath}/all", method = RequestMethod.GET)
    public ResponseEntity<List<${concept_name}>> getAll${concept_name}Data()
    throws Exception {
        List<${concept_name}> ${concept_varname}List = repository.findAll();
        ${concept_varname}List = ${concept_varname}List.stream()
                .filter(e -> e.getIsActive())
                .collect(Collectors.toList());
        LOG.info("SELECT ${concept_name} all");
        return ResponseEntity.ok().body(${concept_varname}List);
    }

    @RequestMapping(value = "/${concept_apipath}/page/{page}", method = RequestMethod.GET)
    public ResponseEntity<List<${concept_name}>> getAllProductByPage(
        @PathVariable(value = "page") Integer pageNum,
        @RequestParam(value = "perPage", defaultValue = "10") Integer perPage)
        throws Exception {

        if(pageNum == null || pageNum <= 0)
            pageNum = 1;
        Integer offset = (pageNum - 1) * perPage;

        List<${concept_name}> ${concept_varname}List = repository.findAllByLimitAndOffset(perPage, offset);
        LOG.info("SELECT ${concept_name} where pageNum=" + pageNum);
        return ResponseEntity.ok().body(${concept_varname}List);
    }